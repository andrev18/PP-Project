package model;

import javax.lang.model.element.Element;
import java.util.Collections;
import java.util.Set;

public class BaoHolder {
    private final Element baoElement;
    private final Set<? extends Element> insertElements = Collections.emptySet();
    private final Set<? extends Element> deleteElements = Collections.emptySet();

    public BaoHolder(Element baoElement) {
        this.baoElement = baoElement;
    }

    public Element getBaoElement() {
        return baoElement;
    }

    public Set<? extends Element> getInsertElements() {
        return insertElements;
    }

    public Set<? extends Element> getDeleteElements() {
        return deleteElements;
    }
}
