package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Component;

@Component
public class CommandStart implements Command {
    @Override
    public String command() {
        return "/start";
    }

    @Override
    public String description() {
        return "регистрирует нового пользователя.";
    }

    @Override
    public SendMessage handle(Update update) {
        long chatId = update.message().chat().id();
        if (update.message().text().equals(command())) {
            return new SendMessage(
                chatId,
                "Хей)\nВы запустили бота для *отслеживания изменений* на GitHub и"
                    + " StackOverflow. Для *подробного* описания всех комманд введите /help."
            );
        }
        return new SendMessage(chatId, "Для начала работы введите *только* команду /start.");
    }
}
