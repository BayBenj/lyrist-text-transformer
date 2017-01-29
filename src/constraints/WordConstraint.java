package constraints;

import elements.Word;
import filters.ReturnType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

public abstract class WordConstraint extends Constraint {

    //has access to FilterMethods

    protected ReturnType returnType = null;
    protected boolean enforced = false;
    protected boolean enabled = true;
    protected boolean instanceSpecific = false;

    public WordConstraint(ReturnType returnType) {
        this.returnType = returnType;
    }

    public abstract Set<Word> useWithPresetFields(Collection<Word> wordsToFilter);

    public abstract Set<Word> useInstanceSpecific(Collection<Word> wordsToFilter, Word specificWord);

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~Equals & contains objs w/ returnTypes~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    protected static Collection getContained(Collection<Object> modelObjects, Collection<Object> objectsGettingFiltered, ReturnType returnType) {
        Collection result = new ArrayList();
        for (Object o : objectsGettingFiltered)
            if (doesContain(o, modelObjects, returnType))
                result.add(o);
        return result;
    }

    protected static Collection getContained(Object modelObject, Collection<Object> objectsGettingFiltered, ReturnType returnType) {
        Collection result = new ArrayList();
        for (Object o : objectsGettingFiltered)
            if (doesEqual(o, modelObject, returnType))
                result.add(o);
        return result;
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~Equals & contains bools w/ returnTypes~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    protected static boolean doesContain(Object o, Collection<Object> c, ReturnType returnType) {
        if (returnType == ReturnType.MATCHES && c.contains(o) ||
                returnType == ReturnType.NON_MATCHES && !c.contains(o))
            return true;
        return false;
    }

    protected static boolean doesEqual(Object o1, Object o2, ReturnType returnType) {
        if (returnType == ReturnType.MATCHES && o1.equals(o2) ||
                returnType == ReturnType.NON_MATCHES && !o1.equals(o2))
            return true;
        return false;
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~Weaken~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public abstract boolean weaken();

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~Getters/setters~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public ReturnType getReturnType() {
        return returnType;
    }

    public void setReturnType(ReturnType returnType) {
        this.returnType = returnType;
    }

    public boolean isEnforced() {
        return enforced;
    }

    public void setEnforced(boolean enforced) {
        this.enforced = enforced;
    }

    public void enforce() {
        this.enforced = true;
    }

    public void unEnforce() {
        this.enforced = false;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void enable() {
        this.enabled = true;
    }

    public void disable() {
        this.enabled = false;
    }

    public boolean isInstanceSpecific() {
        return instanceSpecific;
    }

    public void setInstanceSpecific(boolean instanceSpecific) {
        this.instanceSpecific = instanceSpecific;
    }

}

































































