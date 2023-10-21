package com.interswitch.Unsolorockets.chatbot;

import jakarta.servlet.ServletContext;
import lombok.extern.slf4j.Slf4j;
import org.alicebot.ab.Bot;
import org.alicebot.ab.Chat;
import org.alicebot.ab.History;
import org.alicebot.ab.MagicBooleans;
import org.alicebot.ab.MagicStrings;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.util.HtmlUtils;

import java.io.File;

@Slf4j
@RestController
//@CrossOrigin(origins = "http://localhost:5174")
public class ChatbotController implements ServletContextAware {

	private ServletContext servletContext;
	
	private static final boolean TRACE_MODE = false;
	Bot bot;
	Chat chatSession;
	
	private static boolean InitFlag = true;
	
	public void botInit() {
		//String resourcesPath = servletContext.getRealPath("/src/main/resources");
		File currDir = new File(".");
		String path = currDir.getAbsolutePath();
		path = path.substring(0, path.length() - 2);
		System.out.println(path);
		String resourcesPath = path.concat("/src/main/resources");
		System.out.println(resourcesPath);
		MagicBooleans.trace_mode = TRACE_MODE;
		bot = new Bot("super", resourcesPath);
		chatSession = new Chat(bot);
		bot.brain.nodeStats();
	}
	
	@MessageMapping("/talktorobot")
	@SendTo("/topic/public")
	public ChatMessage greeting(@Payload ChatMessage message) throws Exception {
		String response = "";

		try {
			if(BooleanUtils.isTrue(InitFlag)) {
				botInit();
				System.out.println("BotInitialized");
				InitFlag = false;
			}

			String textLine = message.getMessage();
			System.out.println("Human : " + textLine);

			if ((textLine == null) || (textLine.length() < 1)) {
				textLine = MagicStrings.null_input;
			}

			if (textLine.equals("q")) {
				System.exit(0);
			} else if (textLine.equals("wq")) {
				bot.writeQuit();
				System.exit(0);
			} else {
				if (MagicBooleans.trace_mode)
					System.out.println("STATE=NA:THAT="
							+ ((History<?>) chatSession.thatHistory.get(0)).get(0)
							+ ":TOPIC=" + chatSession.predicates.get("topic"));

				response = chatSession.multisentenceRespond(textLine);

				while (response.contains("&lt;"))
					response = response.replace("&lt;", "<");
				while (response.contains("&gt;"))
					response = response.replace("&gt;", ">");

				response = executeDefault(response);

				System.out.println("Robot : " + response);
			}
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		Thread.sleep(1000); // simulated delay
		System.out.println(response);
		return new ChatMessage(MessageType.CHAT, HtmlUtils.htmlEscape(response), "Olly");
	}

	private String executeDefault(String response) {
		if(response.trim().contains("<oob><url>")){
			response = setOOBUrl(response);
		}
		if(response.trim().contains("<oob><search>")){
			response = setOOBSearch(response);
		}
		return response;
	}
	
	private String setOOBUrl(String response)
	{
		String bettext = StringUtils.substringBetween(response, "<oob><url>", "</url></oob>");
		response = response.replace("<oob><url>", "<a href=\"");
		response = response.replace("</url></oob>", "\" target=\"_blank\">" + bettext + "</a>");
		return response;
	}
	
	private String setOOBSearch(String response)
	{
		String bettext = StringUtils.substringBetween(response, "<oob><search>", "</search></oob>");
		response = response.replace("<oob><search>", "<br/><a href=\"https://www.google.com/search?q=");
		response = response.replace("</search></oob>", "\" target=\"_blank\"><i>Click Here to View Result for "+bettext+".</i></a>");
		return response;
	}


	@MessageMapping("/chat.sendMessage")
	@SendTo("/topic/public")
	public ChatMessage sendMessage(
			@Payload ChatMessage chatMessage
	) {
		return chatMessage;
	}

	@MessageMapping("/chat.addUser")
	@SendTo("/topic/public")
	public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
		// Add username in web socket session
		log.info("CHATMESSAGE, {}", chatMessage);
		headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
		return chatMessage;
	}

	@Override
	public void setServletContext(ServletContext servletContext) {
		// TODO Auto-generated method stub
		this.servletContext = servletContext;
	}
	
}
