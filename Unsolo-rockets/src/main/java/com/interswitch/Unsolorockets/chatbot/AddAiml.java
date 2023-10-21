package com.interswitch.Unsolorockets.chatbot;

import org.alicebot.ab.Bot;
import org.alicebot.ab.MagicBooleans;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class AddAiml {

	private static final boolean TRACE_MODE = false;
	static String botName = "super";

	@Bean
	CommandLineRunner commandLineRunner() {
		return args -> {
			try {

				String resourcesPath = getResourcesPath();
				MagicBooleans.trace_mode = TRACE_MODE;
				Bot bot = new Bot("super", resourcesPath);

				bot.writeAIMLFiles();

			} catch (Exception e) {
				e.printStackTrace();
			}
		};
	}

	private static String getResourcesPath() {
		File currDir = new File(".");
		String path = currDir.getAbsolutePath();
		path = path.substring(0, path.length() - 2);
		System.out.println(path);
		String resourcesPath = path.concat("/src/main/resources");
		System.out.println(resourcesPath);
		return resourcesPath;
	}

}
