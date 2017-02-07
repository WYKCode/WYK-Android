package college.wyk.app.model.directus;

import org.parceler.Parcel;

import java.util.List;

@Parcel
public class PublicationStack {

    public List<Publication> items;

    public PublicationStack() {
    }

    public PublicationStack(List<Publication> items) {
        this.items = items;
    }

}
