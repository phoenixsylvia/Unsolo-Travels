package com.interswitch.Unsolorockets.service.payment;

import com.interswitch.Unsolorockets.exceptions.CommonsException;
import com.interswitch.Unsolorockets.exceptions.UserNotFoundException;
import com.interswitch.Unsolorockets.models.Traveller;
import com.interswitch.Unsolorockets.models.Wallet;
import com.interswitch.Unsolorockets.models.enums.PaymentStatus;
import com.interswitch.Unsolorockets.respository.TransactionRepository;
import com.interswitch.Unsolorockets.respository.TravellerRepository;
import com.interswitch.Unsolorockets.respository.WalletRepository;
import com.interswitch.Unsolorockets.utils.IAppendableReferenceUtils;
import com.interswitch.Unsolorockets.utils.CustomUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.interswitch.Unsolorockets.utils.UserUtil.getLoggedInUser;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final FlutterwaveService flutterwaveService;

    private final TravellerRepository travellerRepository;

    private final WalletRepository walletRepository;

    private final TransactionRepository transactionRepository;

    @Transactional
    public PaymentInitiationResponse initiatePayment(PaymentRequestDto paymentRequestDto) throws CommonsException {
        CustomUser user = getLoggedInUser();
        long userId = user.getId();
        Traveller traveller = travellerRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        Customer customer = new Customer();
        customer.setName(traveller.getFirstName() + " " + traveller.getLastName());
        customer.setEmail(traveller.getEmail());
        customer.setPhoneNo(traveller.getPhoneNumber());
        paymentRequestDto.setCustomer(customer);
        PaymentLogDto paymentLogDto = createPaymentLog(userId, paymentRequestDto);
        paymentRequestDto.setPaymentReference(paymentLogDto.getReference());

        PaymentInitiationResponse response = flutterwaveService.generatePaymentLink(paymentRequestDto);
        response.setCurrency(paymentRequestDto.getCurrencyCode());
        updatePaymentLog(userId, paymentLogDto.getReference(), response);
        return response;

    }

    private void updatePaymentLog(Long userId, String reference, PaymentInitiationResponse response) throws CommonsException {
        long id = IAppendableReferenceUtils.getIdFrom(reference);
        Transaction transaction = transactionRepository.findByIdAndUserId(id, userId).orElseThrow(() -> new CommonsException("transaction does not exist", HttpStatus.NOT_FOUND));
        transaction.setProcessorReference(response.getProcessorReference());
        transaction.setProcessor(response.getProcessor().name());
        transaction.setStatus(response.getStatus());
        transaction.setPaymentLink(response.getPaymentLink());
        transactionRepository.save(transaction);
    }

    private PaymentLogDto createPaymentLog(Long userId, PaymentRequestDto paymentRequestDto) {
        Transaction transaction = new Transaction();
        transaction.setUserId(userId);
        transaction.setTransactionReference(transaction.getReference());
        BeanUtils.copyProperties(paymentRequestDto, transaction);
        transaction.setStatus(PaymentStatus.NEW);

        transaction = transactionRepository.save(transaction);

        return PaymentLogDto.fromPaymentLog(transaction);
    }


    public PaymentLogDto getPayment(String reference) throws CommonsException {
        return getPayment(IAppendableReferenceUtils.getIdFrom(reference));
    }

    public PaymentLogDto getPayment(long id) throws CommonsException {

        Transaction cardPayments = transactionRepository.findById(id).orElseThrow(() -> new CommonsException("payment does not exist", HttpStatus.NOT_FOUND));
        //perform deep requery for pending payment
        if (cardPayments.getStatus() == PaymentStatus.PENDING) {
            cardPayments = requeryPayment(cardPayments);
        }
        return PaymentLogDto.fromPaymentLog(cardPayments);

    }

    private Transaction requeryPayment(Transaction payment) {
        try {
            FlutterwavePaymentRequeryResponseDto requeryResponseDto = flutterwaveService.getStatus(payment.getReference());
            //when status changes from processor, update payment log
            if (requeryResponseDto.data.status.equalsIgnoreCase("successful")) {
                payment.setProcessorReference(payment.getReference());
                payment.setStatus(PaymentStatus.SUCCESSFUL);
                Wallet wallet = walletRepository.findByUserId(payment.getUserId()).orElseThrow(() -> new CommonsException("user does not have a wallet", HttpStatus.NOT_FOUND));
                wallet.setUserId(payment.getUserId());
                wallet.setBalance(wallet.getBalance().add(payment.getAmount()));
                walletRepository.save(wallet);
            } else if (requeryResponseDto.data.status.equalsIgnoreCase("failed")) {
                payment.setStatus(PaymentStatus.FAILED);

            }
            transactionRepository.save(payment);

            return payment;
        } catch (CommonsException ex) {
            log.error("requeryCardPayment reference:[{}] error: {}", payment.getReference(), ex.getMessage());
            return payment;
        }
    }
}
