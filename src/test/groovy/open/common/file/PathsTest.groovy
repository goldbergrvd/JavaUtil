package open.common.file

import spock.lang.Specification

/**
 * @author Hubert Lu <goldbergrvd@gmail.com>
 */
class PathsTest extends Specification {
    def "路徑樣板建立測試"() {
        expect:
            "/data/myapp/Z/B/2015/02/20150201.csv" == Paths.pattern("/data/myapp/{1}/{2}/{yyyy}/{MM}/{yyyy}{MM}{dd}.csv")
                                                           .withDate(Date.parse("yyyyMMdd", "20150201"))
                                                           .addSegment("Z")
                                                           .addSegment("B")
                                                           .build()

            "/data/myapp/ORG001/2015/02/ORG00120150201.csv" == Paths.pattern("/data/myapp/{1}/{yyyy}/{MM}/{1}{yyyy}{MM}{dd}.csv")
                                                                    .withDate(Date.parse("yyyyMMdd", "20150201"))
                                                                    .addSegment("ORG001")
                                                                    .build()

            "/data/myapp/Z/B/2015/02/20150201.csv" == Paths.pattern("/data/myapp/{1}/{2}/{yyyy}/{MM}/{yyyy}{MM}{dd}.csv")
                                                           .withYear("2015")
                                                           .withMonth("02")
                                                           .withDay("01")
                                                           .addSegment("Z")
                                                           .addSegment("B")
                                                           .build()

            "/data/myapp/ORG001/2015/02/ORG00120150201.csv" == Paths.pattern("/data/myapp/{1}/{yyyy}/{MM}/{1}{yyyy}{MM}{dd}.csv")
                                                                    .withYear("2015")
                                                                    .withMonth("02")
                                                                    .withDay("01")
                                                                    .addSegment("ORG001")
                                                                    .build()
    }
}