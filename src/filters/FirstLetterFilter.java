package filters;

import song.CharList;

import java.util.HashSet;
import java.util.Set;

public class FirstLetterFilter extends CharFilter {

    public FirstLetterFilter(CharList charList) {
        super(charList);
    }

    public FirstLetterFilter(ReturnType returnType, CharList charList) {
        super(returnType, charList);
    }

    @Override
    public Set<String> doFilter(Set<String> originalStrings) {
        Set<String> result = new HashSet<>();
        for (String s : originalStrings) {
            if (s != null && s.length() > 0) {
                char c = s.charAt(0);
                if (super.getReturnType() == ReturnType.MATCHES && (super.getCharList().contains(Character.toLowerCase(c)) || super.getCharList().contains(Character.toUpperCase(c)))) {
                    result.add(s);
                }
                else if (super.getReturnType() == ReturnType.NON_MATCHES && !(super.getCharList().contains(Character.toLowerCase(c)) || super.getCharList().contains(Character.toUpperCase(c)))) {
                    result.add(s);
                }
            }
        }
        return result;
    }
}























































































