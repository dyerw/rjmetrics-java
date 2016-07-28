## RJMetrics Java

This library is a simple wrapper around the RJMetrics Pipeline API described [here](http://developers.rjmetrics.com/pipeline/api.html)

### Usage

```java

// Create a class for whatever data that has fields for every column in your Redshift table
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

// Create a new instance of RJMetrics and give it your API key and your client id
RJMetrics<DummyData> rjMetrics = new RJMetrics<DummyData>("API KEY", 666);

// Create a list of as many objects as you want
ArrayList<DummyData> dummyDataList = new ArrayList<DummyData>();
dummyDataList.add(new DummyData("a", 1));
dummyDataList.add(new DummyData("b", 2));
dummyDataList.add(new DummyData("c", 3));


// Use the upsert function to get them into Redshift :)
rjMetrics.upsert(dummyDataList, "dummy_data", new ArrayList<String>(Arrays.asList("id")), new UpsertCompletionHandler() {
    @Override
    public void onCompleted(String response) {
        System.out.println(response);
    }
});
```