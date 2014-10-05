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
            result.eachWithIndex { bean, i ->
                "value" + (i+1) + "-1" == bean.value1
                "value" + (i+1) + "-2" == bean.value2
                "value" + (i+1) + "-3" == bean.value3
                "value" + (i+1) + "-4" == bean.value4
            }
    }
    
    public static class Bean {
        @JsonProperty(index=0) public String value1;
        @JsonProperty(index=1) public String value2;
        @JsonProperty(index=2) public String value3;
        @JsonProperty(index=3) public String value4;
    }
}
