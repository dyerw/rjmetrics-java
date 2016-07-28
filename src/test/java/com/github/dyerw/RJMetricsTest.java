package com.github.dyerw;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class RJMetricsTest {
    @Test
    public void testUpsert() throws Exception {
        class DummyData {
            private String id;
            private int bar;

            public DummyData(String id, int bar) {
                this.id = id;
                this.bar = bar;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public int getBar() {
                return bar;
            }

            public void setBar(int bar) {
                this.bar = bar;
            }
        }

        RJMetrics<DummyData> rjMetrics = new RJMetrics<DummyData>("API KEY", 666);

        ArrayList<DummyData> dummyDataList = new ArrayList<DummyData>();
        dummyDataList.add(new DummyData("a", 1));
        dummyDataList.add(new DummyData("b", 2));
        dummyDataList.add(new DummyData("c", 3));

        rjMetrics.upsert(dummyDataList, "dummy_data", new ArrayList<String>(Arrays.asList("id")));

        Thread.sleep(10000);
    }
}
