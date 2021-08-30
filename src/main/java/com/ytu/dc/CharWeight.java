import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CharWeight implements Comparable<CharWeight> {

    private Character character;
    private Integer weight;

    @Override
    public int compareTo(CharWeight u) {
        Integer first = Math.abs(getWeight());
        Integer last = Math.abs(u.getWeight());
        return first.compareTo(last);
    }
}
