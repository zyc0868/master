package common;

/**
 * @author: flyboy
 * @Date: 20/03/2021 15:09
 * @Version 1.0
 * @Email: 1308794149@qq.com
 */
public interface Constants {
    String serverAddress = "localhost";
    int serverPort = 9000;
    String serverName = "admin";

    String serverSystemCommandLogOut = "logout";
    String serverSystemCommandUserList = "list";
    String serverSystemCommandOperation= "operation";
    String chatStartSymbol = "@";
    String validReceiverName = "ok";

    String userRegisterValid = "allow register";

    char message_sep_char = Character.UNASSIGNED;
    String message_sep_string = String.valueOf(Character.UNASSIGNED);

    String nameForRegister = "tempName";

    String operationGuide = "if want talk with someone use @name to start a chat. " +
            "if want to get operation guide type @admin operation. "+
            "if want to log out use @admin logout.";
}
