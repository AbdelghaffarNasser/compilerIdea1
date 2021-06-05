package project;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class SuggestString {

    final private String[] defaultWords = {"Yesif-Otherwise ", "Omw ", "SIMww ", "Chji ", "Seriestl ", "IMwf ", "SIMwf ", "NOReturn ", "RepeatWhen ", "Reiterate ", "GetBack ", "OutLoop ", "Loli ", "Include ", "Start ", "Last "};
    public List<String> words = new ArrayList<String>(Arrays.asList(defaultWords));
    public String[] lastResult = {""};

    public String[] serche(String text) {
        List<String> result = new ArrayList<String>();
        Pattern p = Pattern.compile("([a-zA-Z])(.*([A-Z])[a-z]+)*");
        for (String str : words.toArray(new String[words.size()])) {
            Matcher m = p.matcher(str);
            if (m.find() && text.matches("(" + m.group(1) + ".*(?i)" + m.group(3) + "(?-i).*)|(" + str.substring(0, Math.min(text.length(), str.length())) + ".*)")) {
                result.add(str);
            }
        }
        lastResult = result.toArray(new String[result.size()]);
        return lastResult;
    }

}
