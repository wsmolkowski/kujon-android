package mobi.kujon.network.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 *
 */

public class SimpleUserJson {

    @SerializedName("first_name")
    @Expose
    public String first_name;
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("last_name")
    @Expose
    public String last_name;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(first_name).append(id).append(last_name).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof SimpleUserJson) == false) {
            return false;
        }
        SimpleUserJson rhs = ((SimpleUserJson) other);
        return new EqualsBuilder().append(first_name, rhs.first_name).append(id, rhs.id).append(last_name, rhs.last_name).isEquals();
    }

}