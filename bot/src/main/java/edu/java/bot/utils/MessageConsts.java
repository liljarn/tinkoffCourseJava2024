package edu.java.bot.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MessageConsts {
    public static final String START_COMMAND = "/start";
    public static final String HELP_COMMAND = "/help";
    public static final String TRACK_COMMAND = "/track";
    public static final String UNTRACK_COMMAND = "/untrack";
    public static final String LIST_COMMAND = "/list";
    public static final String START_DESCRIPTION = "Зарегистрировать нового пользователя.";
    public static final String HELP_DESCRIPTION = "Вывести список команд.";
    public static final String TRACK_DESCRIPTION = "Добавить отслеживаемый ресурс.";
    public static final String UNTRACK_DESCRIPTION = "Удалить отслеживаемый ресурс.";
    public static final String LIST_DESCRIPTION = "Вывести список отслеживаемых ресурсов.";
    public static final String GITHUB_LINK = "https://github.com/liljarn/tinkoffCourseJava2024";
    public static final String STACK_LINK =
        "https://stackoverflow.com/questions/53579112/inject-list-of-all-beans-with-a-certain-interface";
    public static final String SUCCESSFUL_DELETE = "Ссылка успешно удалена.";
    public static final String NOT_COMMAND =
        "Не понимаю :(, напишите команду /help для вывода *списка* доступных команд.";
    public static final String NOT_TEXT =
        "*Пожалуйста*, не отправляйте что-либо кроме текста, мне тяжело это обрабатывать :(";
    public static final String HELP_COMMANDS_LIST = "Список команд:\n";
    public static final String HELP_WRONG_TEXT = "Для вывода всех команд введите *только* команду /help.";
    public static final String LIST_COMMANDS_TEXT = "*Вот список отслеживаемых страниц:*\n";
    public static final String START_MESSAGE = "Хей)\nВы запустили бота для *отслеживания изменений* на GitHub и"
        + " StackOverflow. Для *подробного* описания всех комманд введите /help.";
    public static final String START_WRONG_TEXT = "Для начала работы введите *только* команду /start.";
    public static final String TRACK_COMMAND_ONLY = "Введите команду /track и *ссылку* на ресурс.";
    public static final String TRACK_WRONG_TEXT =
        "*Неверно* введена *команда* /track или *сслыка*. Введите команду /track и ссылку на ресурс раздельно."
            + " Ссылка должна начинаться с https://";
    public static final String URL_START = "https://";
    public static final String UNTRACK_MESSAGE = "Выберите *страницу*, которую хотите перестать отслеживать.";
    public static final String UNTRACK_WRONG_TEXT = "Введите *только* команду /untrack.";
    public static final String LIST_WRONG_TEXT = "Введите *только* команду /list.";
}
