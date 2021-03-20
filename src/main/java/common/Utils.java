package common;

/**
 * @author: flyboy
 * @Date: 20/03/2021 21:20
 * @Version 1.0
 * @Email: 1308794149@qq.com
 */
public class Utils {
    public static String isValidToUserName(String userName) {
        if (userName.trim().length() == 0) {
            return "user name can't be space";
        }
        if (userName.contains(Constants.chatStartSymbol)) {
            return "user name can't contains " + Constants.chatStartSymbol;
        }
        return Constants.validReceiverName;
    }
}
