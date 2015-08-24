package com.esotericsoftware.kryo;

import com.esotericsoftware.kryo.io.FastOutput;
import com.esotericsoftware.minlog.Log;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.util.*;

public class StackOverflowTest {

    @Test
    public void goBananas_foo() {
        FooRef fooRef = new FooRef();
        GenericFoo<FooRef> genFoo1 = new GenericFoo<FooRef>(fooRef);
        GenericFoo<FooRef> genFoo2 = new GenericFoo<FooRef>(fooRef);
        List<GenericFoo<?>> foos = new ArrayList<GenericFoo<?>>();
        foos.add(genFoo2);
        foos.add(genFoo1);
        new FooContainer(foos);
        Kryo kryo = new Kryo();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        kryo.writeObject(new FastOutput(outputStream), genFoo1);
    }

    @Test
    public void goBananas_bar() {
        BarRef barRef = new BarRef();
        GenericBar<BarRef> genBar1 = new GenericBar<BarRef>(barRef);
        GenericBar<BarRef> genBar2 = new GenericBar<BarRef>(barRef);
        List<GenericBar<?>> bars = new ArrayList<GenericBar<?>>();
        bars.add(genBar2);
        bars.add(genBar1);
        new GenericBarContainer<GenericBar>(new BarContainer(bars));
        Kryo kryo = new Kryo();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        Log.set(Log.LEVEL_TRACE);
        kryo.writeObject(new FastOutput(outputStream), genBar1);
    }
}

class GenericBarContainer<T extends Bar> {
    BarContainer barContainer;

    public GenericBarContainer(BarContainer barContainer) {
        this.barContainer = barContainer;
        for (GenericBar<?> foo : barContainer.foos) {
            foo.container = this;
        }
    }
}
class BarContainer {
    List<GenericBar<?>> foos;

    public BarContainer(List<GenericBar<?>> foos) {
        this.foos = foos;
    }
}
interface Bar {}
class BarRef implements Bar {}
class GenericBar<B extends Bar> implements Bar {
    private Map<String, Object> map = Collections.singletonMap("myself", (Object) this);
    B foo;
    GenericBarContainer<?> container;

    public GenericBar(B foo) {
        this.foo = foo;
    }
}

interface Foo {}
class FooRef implements Foo {}
class FooContainer {
    List<GenericFoo<?>> foos = new ArrayList<GenericFoo<?>>();

    public FooContainer(List<GenericFoo<?>> foos) {
        this.foos = foos;
        for (GenericFoo<?> foo : foos) {
            foo.container = this;
        }
    }
}
class GenericFoo<B extends Foo> implements Foo {
    private Map<String, Object> map = Collections.singletonMap("myself", (Object) this);
    B foo;
    FooContainer container;

    public GenericFoo(B foo) {
        this.foo = foo;
    }
}
