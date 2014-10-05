package open.common.json;

import spock.lang.Specification;

/**
 * 
 * @author Hubert Lu <goldbergrvd@gmail.com>
 */
public class JSONsTest extends Specification {

    def "JSONs transform from json2DArray to List"() {
        given:
            def json = '''[["value1-1","value1-2","value1-3","value1-4"],
                        ["value2-1","value2-2","value2-3","value2-4"],
                        ["value3-1","value3-2","value3-3","value3-4"],
                        ["value4-1","value4-2","value4-3","value4-4"]]'''
        
        when:
            def result = JSONs.transform(json, Bean.class)
        
        then:
            "value${a}-1" == result.get(b).value1
            "value${a}-2" == result.get(b).value2
            "value${a}-3" == result.get(b).value3
            "value${a}-4" == result.get(b).value4

        where:
            a || b
            1 || 0
            2 || 1
            3 || 2
            4 || 3
    }
    
    public static class Bean {
        @JsonProperty(index=0) public String value1;
        @JsonProperty(index=1) public String value2;
        @JsonProperty(index=2) public String value3;
        @JsonProperty(index=3) public String value4;
    }
}
