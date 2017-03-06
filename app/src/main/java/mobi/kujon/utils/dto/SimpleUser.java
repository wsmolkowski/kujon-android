package mobi.kujon.utils.dto;

import mobi.kujon.network.json.SimpleUserJson;

/**
 *
 */

public class SimpleUser {

    private String name,id;

    public SimpleUser(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public SimpleUser(SimpleUserJson author){
        this(author.first_name + " " + author.last_name,author.id);

    }


    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }
}
