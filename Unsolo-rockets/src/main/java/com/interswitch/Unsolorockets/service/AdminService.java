package com.interswitch.Unsolorockets.service;

import com.interswitch.Unsolorockets.dtos.requests.PackageDto;
import com.interswitch.Unsolorockets.exceptions.PackageException;
import com.interswitch.Unsolorockets.exceptions.UserAlreadyExistException;
import com.interswitch.Unsolorockets.exceptions.UserNotFoundException;

public interface AdminService {
    String createPackage(PackageDto packageDto) throws PackageException;
    String deactivateUserAccount(String email) throws UserAlreadyExistException, UserNotFoundException;
}
