package info.germanvaldesdev.email.utils;

import java.util.regex.Pattern;

public class HtmlValidator {

    public static boolean isHtml(final String input) {
        Pattern htmlPattern = Pattern.compile(".*\\<[^>]+>.*", Pattern.DOTALL);
        return htmlPattern.matcher(input).matches();
    }
}
