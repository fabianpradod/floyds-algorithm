package com.hd10.floyd;

public class Path {
    private final String name;
    private final String city1;
    private final String city2;
    private final int normalTime;
    private final int rainTime;
    private final int snowTime;
    private final int stormTime;

    public Path(String name,
                String city1,
                String city2,
                int normalTime,
                int rainTime,
                int snowTime,
                int stormTime) {
        this.name = name;
        this.city1 = city1;
        this.city2 = city2;
        this.normalTime = normalTime;
        this.rainTime = rainTime;
        this.snowTime = snowTime;
        this.stormTime = stormTime;
    }

    public String getName()      { return name; }
    public String getCity1()     { return city1; }
    public String getCity2()     { return city2; }
    public int    getNormalTime(){ return normalTime; }
    public int    getRainTime()  { return rainTime; }
    public int    getSnowTime()  { return snowTime; }
    public int    getStormTime() { return stormTime; }
}
