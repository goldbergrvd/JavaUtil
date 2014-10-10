package open.common.collection

import open.common.collection.annotation.GroupKey
import spock.lang.Specification

/**
 * @author Hubert Lu <goldbergrvd@gmail.com>
 */
public class GrouperTest extends Specification {

    def "測試成功分群"() {
        given:
            List<Bean> groupMembers = [new Bean(value1: "g1", value2: "g1", value3: "Bean1"),
                                       new Bean(value1: "g1", value2: "g2", value3: "Bean2"),
                                       new Bean(value1: "g2", value2: "g2", value3: "Bean3"),
                                       new Bean(value1: "g1", value2: "g1", value3: "Bean4"),
                                       new Bean(value1: "g1", value2: "g2", value3: "Bean5"),
                                       new Bean(value1: "g2", value2: "g2", value3: "Bean6"),
                                       new Bean(value1: "g1", value2: "g1", value3: "Bean7"),
                                       new Bean(value1: "g2", value2: "g2", value3: "Bean8"),
                                       new Bean(value1: "g1", value2: "g2", value3: "Bean9"),
                                       new Bean(value1: "g2", value2: "g2", value3: "Bean10"),
                                       new Bean(value1: "g1", value2: "g2", value3: "Bean11"),
                                       new Bean(value1: "g1", value2: "g1", value3: "Bean12")
                                      ]

        when:
            Map<String, List<Bean>> oneKeyResult = Grouper.group(groupMembers)
            def oneKeyGroupG1 = oneKeyResult.get("g1")
            def oneKeyGroupG2 = oneKeyResult.get("g2")

            Map<String, List<Bean>> doubleKeyResult = Grouper.group(groupMembers, "double_key")
            def twoKeyGroupG1G1 = doubleKeyResult.get("g1g1")
            def twoKeyGroupG1G2 = doubleKeyResult.get("g1g2")
            def twoKeyGroupG2G1 = doubleKeyResult.get("g2g1")
            def twoKeyGroupG2G2 = doubleKeyResult.get("g2g2")

        then:
            oneKeyResult.size() == 2

            oneKeyGroupG1.size() == 8
            oneKeyGroupG1.get(0).value3 == "Bean1"
            oneKeyGroupG1.get(1).value3 == "Bean2"
            oneKeyGroupG1.get(2).value3 == "Bean4"
            oneKeyGroupG1.get(3).value3 == "Bean5"
            oneKeyGroupG1.get(4).value3 == "Bean7"
            oneKeyGroupG1.get(5).value3 == "Bean9"
            oneKeyGroupG1.get(6).value3 == "Bean11"
            oneKeyGroupG1.get(7).value3 == "Bean12"

            oneKeyGroupG2.size() == 4
            oneKeyGroupG2.get(0).value3 == "Bean3"
            oneKeyGroupG2.get(1).value3 == "Bean6"
            oneKeyGroupG2.get(2).value3 == "Bean8"
            oneKeyGroupG2.get(3).value3 == "Bean10"

            doubleKeyResult.size() == 3

            twoKeyGroupG1G1.size() == 4
            twoKeyGroupG1G1.get(0).value3 == "Bean1"
            twoKeyGroupG1G1.get(1).value3 == "Bean4"
            twoKeyGroupG1G1.get(2).value3 == "Bean7"
            twoKeyGroupG1G1.get(3).value3 == "Bean12"

            twoKeyGroupG1G2.size() == 4
            twoKeyGroupG1G2.get(0).value3 == "Bean2"
            twoKeyGroupG1G2.get(1).value3 == "Bean5"
            twoKeyGroupG1G2.get(2).value3 == "Bean9"
            twoKeyGroupG1G2.get(3).value3 == "Bean11"

            twoKeyGroupG2G1 == null

            twoKeyGroupG2G2.size() == 4
            twoKeyGroupG2G2.get(0).value3 == "Bean3"
            twoKeyGroupG2G2.get(1).value3 == "Bean6"
            twoKeyGroupG2G2.get(2).value3 == "Bean8"
            twoKeyGroupG2G2.get(3).value3 == "Bean10"

    }

    def "GroupKey 回傳型態非字串的錯誤"() {
        given:
            List<WrongReturnTypeKeyBean> groupMembers = [new WrongReturnTypeKeyBean(),
                                                         new WrongReturnTypeKeyBean(),
                                                         new WrongReturnTypeKeyBean()
                                                        ]

        when:
            Grouper.group(groupMembers)

        then:
            IllegalArgumentException e = thrown()
            e.message == "GroupKey return type must be java.lang.String!!"
    }

    def "GroupKey 重複的錯誤"() {
        given:
            def key = "same_key"
            List<DuplicateKeyBean> groupMembers = [new DuplicateKeyBean(),
                                                   new DuplicateKeyBean(),
                                                   new DuplicateKeyBean()
                                                  ]

        when:
            Grouper.group(groupMembers, key)

        then:
            IllegalArgumentException e = thrown()
            e.message == "GroupKey name \"" + key + "\" duplicate!"
    }

    public static class Bean {
        public String value1;
        public String value2;
        public String value3;

        @GroupKey()
        public String getKey() {
            return value1;
        }

        @GroupKey(name="double_key")
        public String getDoubleKey() {
            return value1 + value2;
        }
    }

    public static class WrongReturnTypeKeyBean {
        @GroupKey()
        public Bean getKey() {
            return new Bean();
        }
    }

    public static class DuplicateKeyBean {
        @GroupKey(name="same_key")
        public String getKey1() {
            return "key1";
        }

        @GroupKey(name="same_key")
        public String getKey2() {
            return "key2";
        }
    }

}