package mobi.kujon.utils.predicates;


import com.github.underscore.Predicate;

import mobi.kujon.network.json.Message;

/**
 *
 */

public class MessagePredicate implements Predicate<Message> {
    private String query;

    public MessagePredicate(String query) {
        this.query = query.toLowerCase();
    }

    @Override
    public Boolean apply(Message arg) {
        return arg.from.toLowerCase().startsWith(query);
    }
}
