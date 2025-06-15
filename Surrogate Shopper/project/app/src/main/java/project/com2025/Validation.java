package project.com2025;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {
    public static boolean validateName(String name) {
        String regex = "^[A-Z]+([-''][A-Z]+)*$";
        return name != null && name.matches(regex);
    }
    public static boolean validatePhoneNumber(String phoneNumber) {
        String regex = "^0[0-9]{9}$";
        return phoneNumber != null && phoneNumber.matches(regex);
    }
    public static boolean emailValidate(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern p = Pattern.compile(emailRegex);
        return email != null && p.matcher(email).matches();
    }
    public static boolean validId(String id) {
        Matcher m = ID_PATTERN.matcher(id);
        if (!m.find()) return false;

        int yy = Integer.parseInt(m.group(1));
        int mm = Integer.parseInt(m.group(2));
        int dd = Integer.parseInt(m.group(3));
        int g = Integer.parseInt(m.group(4));
        int s = Integer.parseInt(m.group(5));
        int c = Integer.parseInt(m.group(6));
        int a = Integer.parseInt(m.group(7));
        int z = Integer.parseInt(m.group(8));

        if (mm < 1 || mm > 12 || dd < 1 || dd > 31) return false;

        char[] cs = id.toCharArray();
        int s1 = (cs[0] - '0') + (cs[2] - '0') + (cs[4] - '0') +
                (cs[6] - '0') + (cs[8] - '0') + (cs[10] - '0');

        int s2 = Integer.parseInt("" + cs[1] + cs[3] + cs[5] + cs[7] + cs[9] + cs[11]);
        s2 *= 2;

        char[] cx = Integer.toString(s2).toCharArray();
        s2 = 0;
        for (char cc : cx) s2 += cc - '0';
        s2 += s1;

        String st = Integer.toString(s2);
        s2 = st.charAt(st.length() - 1) - '0';

        return 10 - s2 == z;
    }
    private static final Pattern ID_PATTERN = Pattern.compile(
            "^(\\d{2})(\\d{2})(\\d{2})(\\d)(\\d{3})([01])(\\d)(\\d)$"
    );
    public static boolean isValidPassword(String password) {
        // Regular expression for password validation
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}$";
        return password.matches(regex);
    }
}
