package open.common.file;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author Hubert Lu <goldbergrvd@gmail.com>
 */
public class Paths {
    public static final String YEAR_PATTERN = "{yyyy}";
    public static final String MONTH_PATTERN = "{MM}";
    public static final String DAY_PATTERN = "{dd}";

    public static PathBuilder pattern(String pattern) {
        return new PathBuilder(pattern);
    }

    public static class PathBuilder {
        private String pattern;
        private String year;
        private String month;
        private String day;
        private List<String> pathSegments = new ArrayList<>();

        public PathBuilder(String pattern) {
            this.pattern = pattern;
        }

        public PathBuilder withDate(Date date) {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            this.year = String.format("%04d", c.get(Calendar.YEAR));
            this.month = String.format("%02d", c.get(Calendar.MONTH) + 1);
            this.day = String.format("%02d", c.get(Calendar.DAY_OF_MONTH));
            return this;
        }

        public PathBuilder withYear(String year) {
            this.year = year;
            return this;
        }

        public PathBuilder withMonth(String month) {
            this.month = month;
            return this;
        }

        public PathBuilder withDay(String day) {
            this.day = day;
            return this;
        }

        public PathBuilder addSegment(String segment) {
            this.pathSegments.add(segment);
            return this;
        }

        public String build() {
            if (pattern == null) {
                throw new IllegalArgumentException("Please give me a path pattern!!");
            }

            String result = this.pattern;

            if (year != null) {
                result = result.replace(YEAR_PATTERN, year);
            }
            if (month != null) {
                result = result.replace(MONTH_PATTERN, month);
            }
            if (day != null) {
                result = result.replace(DAY_PATTERN, day);
            }

            for (int i = 0; i < this.pathSegments.size(); i++) {
                result = result.replace("{" + (i+1) + "}", this.pathSegments.get(i));
            }

            return result;
        }
    }
}
