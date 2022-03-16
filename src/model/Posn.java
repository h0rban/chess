package model;


import static model.ClassicChessUtils.INDEX2FILE;

public class Posn {

    public final int rank;
    public final int file;

    public Posn(int rank, int file) {
        this.rank = rank;
        this.file = file;
    }

    public Posn(Posn posn) {
        this(posn.rank, posn.file);
    }

    public String toBoardString() {
        return String.format("(%s, %d)", INDEX2FILE.get(file), rank + 1);
    }

    @Override
    public String toString() {
        return String.format("(%d, %d)", rank, file);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Posn posn)) {
            return false;
        }
        return rank == posn.rank && file == posn.file;
    }

    /**
     * Returns Cantor's Pairing (https://en.wikipedia.org/wiki/Pairing_function) of this index's
     * coordinates.
     *
     * @return hashcode
     */
    @Override
    public int hashCode() {
        return this.rank + (this.rank + this.file) * (this.rank + this.file + 1) / 2;
    }
}
