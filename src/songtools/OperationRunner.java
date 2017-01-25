package songtools;

import java.util.Collection;

public abstract class OperationRunner {

    public static void forEach(Collection collection, Operation operation) {
        for (Object o : collection) {
            if (operation instanceof FilterOperation) {

            }
            else if (operation instanceof GeneralOperation) {

            }

        }
    }

}
