package open.common.json

import open.common.json.annotation.JsonArrayValue;
import spock.lang.Specification;

/**
 * 
 * @author Hubert Lu <goldbergrvd@gmail.com>
 */
public class JsonsTest extends Specification {

    def "Jsons transform from json2DArray to List success!"() {
        given:
            def json = '''[["string1-1","string1-2","10","5","0.9","100"],
                        ["string2-1","string2-2","9","6.0","100.8","200"],
                        ["string3-1","string3-2","8","7.5","402.6","300"],
                        ["string4-1","string4-2","7","4.497","84.79","400"]]'''

        when:
            def result = Jsons.transform(json, Bean.class)

        then:
            value0 == result.get(index).value0
            value1 == result.get(index).value1
            value2 == result.get(index).value2
            value3 == result.get(index).value3
            value4 == result.get(index).value4
            value5 == result.get(index).value5
            noValue1 == result.get(index).noValue1
            noValue2 == result.get(index).noValue2

        where:
            index|      value0|      value1| value2| value3| value4| value5| noValue1| noValue2
                0| "string1-1"| "string1-2"|     10|      5|    0.9|    100|     null|     null
                1| "string2-1"| "string2-2"|      9|    6.0|  100.8|    200|     null|     null
                2| "string3-1"| "string3-2"|      8|    7.5|  402.6|    300|     null|     null
                3| "string4-1"| "string4-2"|      7|  4.497|  84.79|    400|     null|     null
    }

    def "Jsons transform from json2DArray to List with illegal type, throw IllegalArgumentException!"() {
        given:
            def json = '''[["bean"]]'''

        when:
            result = Jsons.transform(json, WrongBean.class)

        then:
            thrown(IllegalArgumentException)

    }

    public static class Bean {
        @JsonArrayValue(index=0) private String value0;
        @JsonArrayValue(index=1) public String value1;
        @JsonArrayValue(index=5) private int value5;
        @JsonArrayValue(index=3) public double value3;
        @JsonArrayValue(index=2) private int value2;
        @JsonArrayValue(index=4) public double value4;
        @JsonArrayValue(index=6) public String noValue1;
        public Integer noValue2;
    }

    public static class WrongBean {
        @JsonArrayValue(index=0) private Bean value0;
    }

}
