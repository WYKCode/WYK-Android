package college.wyk.app.model.sns;

import org.parceler.Parcel;

import java.util.List;

@Parcel
public class SnsStack {

    public List<SnsPost> items;
    public Long since;
    public boolean noMoreUpdates;

    public SnsStack() {
    }

    public SnsStack(List<SnsPost> items, Long since, boolean noMoreUpdates) {
        this.items = items;
        this.since = since;
        this.noMoreUpdates = noMoreUpdates;
    }

}
