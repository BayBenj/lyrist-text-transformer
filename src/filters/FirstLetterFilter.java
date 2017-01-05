package filters;

import song.CharList;

import java.util.HashSet;
import java.util.Set;

public class FirstLetterFilter extends CharFilter {

    public FirstLetterFilter(CharList charList) {
        super(charList);
    }

    public FirstLetterFilter(Direction direction, CharList charList) {
        super(direction, charList);
    }

    @Override
    public Set<String> doFilter(Set<String> originalStrings) {
        Set<String> result = new HashSet<>();
        for (String s : originalStrings) {
            if (s != null && s.length() > 0) {
                char c = s.charAt(0);
                if (super.getDirection() == Direction.INCLUDE_MATCH && (super.getCharList().contains(Character.toLowerCase(c)) || super.getCharList().contains(Character.toUpperCase(c)))) {
                    result.add(s);
                }
                else if (super.getDirection() == Direction.EXCLUDE_MATCH && !(super.getCharList().contains(Character.toLowerCase(c)) || super.getCharList().contains(Character.toUpperCase(c)))) {
                    result.add(s);
                }
            }
        }
        return result;
    }
}























































































