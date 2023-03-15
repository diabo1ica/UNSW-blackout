package unsw.blackout;
import unsw.utils.MathsHelper;

interface ConnectionBehaviour {
    public default boolean isValidTransferType(Machine m) {
        return true;
    }

    public default boolean isInRange(Machine m1, Machine m2) {
        return MathsHelper.getDistance(m1.getHeight(), m1.getPos(), m1.getHeight(), m2.getPos()) >= 
        Math.min(m1.getRange(), m2.getRange());
    }

    public boolean visibleInRange(Machine m);
}
