package Ships;

public class Destroyer implements Ship {

    private final int shipLength = 4;

    @Override
    public int getShipLength() {
        return shipLength;
    }
}
