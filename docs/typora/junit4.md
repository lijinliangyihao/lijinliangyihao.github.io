#### Assertions

`org.junit.Assert` 类里有一些简单的 assert 方法

它有个 assertThat 方法，接收一个 hamcrest 的 Matcher

```java
  @Test
  public void testAssertThatBothContainsString() {
    assertThat("albumen", both(containsString("a")).and(containsString("b")));
  }
```



#### Test runners

从 main 方法运行一个测试类（一般没人这么干）

```java
org.junit.runner.JUnitCore.runClasses(TestClass1.class, ...);
```

命令行跑：

```java
java org.junit.runner.JUnitCore TestClass1 [...other test classes...]
```

#### @RunWith annotation

> When a class is annotated with `@RunWith` or extends a class annotated with `@RunWith`, JUnit will invoke the class it references to run the tests in that class instead of the runner built into JUnit

- The default runner is `BlockJUnit4ClassRunner` which supersedes the older `JUnit4ClassRunner`

- Annotating a class with `@RunWith(JUnit4.class)` will always invoke the default JUnit 4 runner in the current version of JUnit, this class aliases the current default JUnit 4 class runner

- `Suite` is a standard runner that allows you to manually build a suite containing tests from many classes:

  ```
  @RunWith(Suite.class)
  // 有序！
  @Suite.SuiteClasses({
    TestFeatureLogin.class,
    TestFeatureLogout.class,
    TestFeatureNavigate.class,
    TestFeatureUpdate.class
  })
  
  public class FeatureTestSuite {
    // the class remains empty,
    // used only as a holder for the above annotations
  }
  ```

-  Once you have annotated certain methods with `@Category(MyCategory.class)`, you can use the `--filter` option to restrict which tests will be run

```java
java org.junit.runner.JUnitCore 
    --filter=org.junit.experimental.categories.IncludeCategories=MyCat1,MyCat2 
    --filter=org.junit.experimental.categories.ExcludeCategories=MyCat3,MyCat4
```



- You may filter tests according to any instance of `FilterFactory`. The `--filter` option takes the general form: 嘛意思呢?

```
java [Runner] --filter=[FilterFactory]=[Categories,]
```

#### Test execution order

From version 4.11, JUnit will by default use a deterministic, but not predictable, order.

往类上加注解修改方法执行顺序。

```
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
// 这个是按照 jvm 返回顺序，有可能每次都不一样
@FixMethodOrder(MethodSorters.JVM)
// 不指定默认是这个
@FixMethodOrder(MethodSorters.DEFAULT)
```

说说 `@OrderWith`

参数是 `Ordering.Factory`。这个包里 `org.junit.tests.manipulation`有一些内置的。

自定义的话，Implementations of `Ordering.Factory` should have a public constructor that takes in a `Ordering.Context`

> The `@OrderWith` annotation works with any runner that implements `Orderable`. The `ParentRunner` abstract class was been retrofitted to implement `Orderable` so the runners provided by JUnit support `@OrderWith`, and many third-party runners will support it

没看见 orderable，只看到 sortable 了



#### Exception testing

```java
IndexOutOfBoundsException thrown = assertThrows(
      IndexOutOfBoundsException.class,
      () -> list.add(1, new Object()))
```



Be aware that `fail()` throws an `AssertionError`, so you cannot use the above idiom to verify that a method call should throw an `AssertionError`

```java
  try {
    list.get(0);
    fail("Expected an IndexOutOfBoundsException to be thrown");
  } catch (IndexOutOfBoundsException anIndexOutOfBoundsException) {
    assertThat(anIndexOutOfBoundsException.getMessage(), is("Index: 0, Size: 0"));
  }
```



不推荐下面这种：

> The `expected` parameter should be used with care. The above test will pass if *any* code in the method throws `IndexOutOfBoundsException`. Using the method you also cannot test the value of the message in the exception, or the state of a domain object after the exception has been thrown

```java
@Test(expected = IndexOutOfBoundsException.class) 
public void empty() { 
  new ArrayList<Object>().get(0); 
}
```



不推荐下面这种：

```java
@Rule
public ExpectedException thrown = ExpectedException.none();

@Test
public void shouldTestExceptionMessage() throws IndexOutOfBoundsException {
  List<Object> list = new ArrayList<Object>();
 
  thrown.expect(IndexOutOfBoundsException.class);
  thrown.expectMessage("Index: 0, Size: 0");
  list.get(0); // execution will never get past this line
}
```

做法是：先 expect，然后执行（抛异常）。因为抛异常后，之后的代码不跑了，所以此种方式不推荐。



#### Ignoring a Test

you can add the `@Ignore` annotation in front or after `@Test`

Note that `@Ignore` takes an optional parameter (a String) if you want to record a reason why a test is being ignored.

```java
@Ignore("Test is ignored as a demonstration")
@Test
public void testSame() {
    assertThat(1, is(1));
}
```



#### Timeout for tests

在另一个线程执行测试：

```java
@Test(timeout=1000)
public void testWithTimeout() {
  ...
}
```



加个 rule，应用到当前类所有 test 上

```java
	@Rule
    public Timeout globalTimeout = Timeout.seconds(10);
```



#### Parameterized tests

类上先加上注解 `@RunWith(Parameterized.class)`；

然后有一个静态方法，返回一个集合或对象数组，上面加注解 `@Parameters`；

效果是每个方法都会执行返回的参数个数次，8 个方法，8组参数，则执行 64 次。

使用构造器注入：

```java
@RunWith(Parameterized.class)
public class FibonacciTest {
    // 注意这个注解
    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {     
                 { 0, 0 }, { 1, 1 }, { 2, 1 }, { 3, 2 }, { 4, 3 }, { 5, 5 }, { 6, 8 }  
           });
    }

    private int fInput;
    private int fExpected;
    // 注意这
    public FibonacciTest(int input, int expected) {
        this.fInput = input;
        this.fExpected = expected;
    }

    @Test
    public void test() {
        assertEquals(fExpected, Fibonacci.compute(fInput));
    }
}
```

另一种方式是不使用构造器，注释很清晰不多说：

```java
@RunWith(Parameterized.class)
public class FibonacciTest {
    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                 { 0, 0 }, { 1, 1 }, { 2, 1 }, { 3, 2 }, { 4, 3 }, { 5, 5 }, { 6, 8 }  
           });
    }

    @Parameter // first data value (0) is default
    public /* NOT private */ int fInput;

    @Parameter(1)
    public /* NOT private */ int fExpected;

    @Test
    public void test() {
        assertEquals(fExpected, Fibonacci.compute(fInput));
    }
}
```

如果不需要多个参数，可以这样：

```java
@Parameters
public static Iterable<? extends Object> data() {
    return Arrays.asList("first test", "second test");
}
```

或 

```java
@Parameters
public static Object[] data() {
    return new Object[] { "first test", "second test" };
}
```



设置每次执行的名字，默认是下标，可以改名：

```java
@RunWith(Parameterized.class)
public class FibonacciTest {

    // 这样，每次执行显示名字为：比如 [3: fib(3)=2] 这样
    @Parameters(name = "{index}: fib({0})={1}")
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][] { 
                 { 0, 0 }, { 1, 1 }, { 2, 1 }, { 3, 2 }, { 4, 3 }, { 5, 5 }, { 6, 8 }
           });
    }

    private int input;
    private int expected;

    public FibonacciTest(int input, int expected) {
        this.input = input;
        this.expected = expected;
    }

    @Test
    public void test() {
        assertEquals(expected, Fibonacci.compute(input));
    }
}
```



#### Assumptions with assume

assume 失败的，被视为 ignored

```java
	import static org.junit.Assume.*
    @Test 
    public void filenameIncludesUsername() {
        assumeThat(File.separatorChar, is('/'));
        assertThat(new User("optimus").configFileName(), is("configfiles/optimus.cfg"));
    }

    @Test 
	public void correctBehaviorWhenFilenameIsNull() {
       assumeTrue(bugFixed("13356"));  // bugFixed is not included in JUnit
       assertThat(parse(null), is(new NullDocument()));
    }
```



#### Rules

内置的 rule

##### TemporaryFolder Rule

The `TemporaryFolder` Rule allows creation of files and folders that are deleted when the test method finishes (whether it passes or fails). By default no exception is thrown if resources cannot be deleted

```java
public static class HasTempFolder {
  @Rule
  public final TemporaryFolder folder = new TemporaryFolder();

  @Test
  public void testUsingTempFolder() throws IOException {
    File createdFile = folder.newFile("myfile.txt");
    File createdFolder = folder.newFolder("subfolder");
    // ...
  }
} 
```

如果创建的临时文件删除不掉不会报错，如果想让它报错：

```java
@Rule 
public TemporaryFolder folder = TemporaryFolder.builder().assureDeletion().build();
```



##### ExternalResource Rules

`ExternalResource` is a base class for Rules (like`TemporaryFolder`) that set up an external resource before a test (a file, socket, server, database connection, etc.), and guarantee to tear it down afterward:

```java
// 用法看起来不太聪明
public static class UsesExternalResource {
  Server myServer = new Server();
  
  @Rule
  public final ExternalResource resource = new ExternalResource() {
    @Override
    protected void before() throws Throwable {
      myServer.connect();
    };
    
    @Override
    protected void after() {
      myServer.disconnect();
    };
  };
  
  @Test
  public void testFoo() {
    new Client().run(myServer);
  }
}
```



##### ErrorCollector Rule

可以收集错误，让报错的测试继续往下执行，然后结束时全打印出来

```java
public static class UsesErrorCollectorTwice {
  @Rule
  public final ErrorCollector collector = new ErrorCollector();
  
  @Test
  public void example() {
    collector.addError(new Throwable("first thing went wrong"));
    collector.addError(new Throwable("second thing went wrong"));
  }
}
```

可以这样用：

```java
	// 注意必须是 public	
	@Rule
    public ErrorCollector ec = new ErrorCollector();
    
    @Test
    public void testAssert() {
        ec.checkThat("a", is("b"));
        ec.checkThat("c", is("d"));
    }
```

这样会报两个错而不是遇到第一个就停止。



##### Verifier Rule

目测意思是运行完测试之后执行这个东西，然后在最后的测试里判断是不是所有测试正常执行了

```java
public static class UsesVerifier {
  
  private static String sequence;
  
  @Rule
  public final Verifier collector = new Verifier() {
    @Override
    protected void verify() {
      sequence += "verify ";
    }
  };

  @Test
  public void example() {
    sequence += "test ";
  }
  
  @Test
  public void verifierRunsAfterTest() {
    sequence = "";
    assertThat(testResult(UsesVerifier.class), isSuccessful());
    assertEquals("test verify ", sequence);
  }

}
```



##### TestWatchman/TestWatcher Rules

类似于一个 listener，运行时对发生的事件做一些处理：

```java
public class WatchmanTest {
  private static String watchedLog;

  @Rule
  public final TestRule watchman = new TestWatcher() {
    @Override
    public Statement apply(Statement base, Description description) {
      return super.apply(base, description);
    }

    @Override
    protected void succeeded(Description description) {
      watchedLog += description.getDisplayName() + " " + "success!\n";
    }

    @Override
    protected void failed(Throwable e, Description description) {
      watchedLog += description.getDisplayName() + " " + e.getClass().getSimpleName() + "\n";
    }

    @Override
    protected void skipped(AssumptionViolatedException e, Description description) {
      watchedLog += description.getDisplayName() + " " + e.getClass().getSimpleName() + "\n";
    }

    @Override
    protected void starting(Description description) {
      super.starting(description);
    }

    @Override
    protected void finished(Description description) {
      super.finished(description);
    }
  };

  @Test
  public void fails() {
    fail();
  }

  @Test
  public void succeeds() {
  }
}
```

##### TestName Rule

运行时能看到当前测试名了：

```java
public class NameRuleTest {
  @Rule
  public final TestName name = new TestName();
  
  @Test
  public void testA() {
    assertEquals("testA", name.getMethodName());
  }
  
  @Test
  public void testB() {
    assertEquals("testB", name.getMethodName());
  }
}
```

##### Timeout Rule

说过了，对每个测试都生效

```java
  @Rule
  public final TestRule globalTimeout = Timeout.millis(20);
```

##### ExpectedException Rules

前面说了，不推荐：

```java
public static class HasExpectedException {
  @Rule
  public final ExpectedException thrown = ExpectedException.none();

  @Test
  public void throwsNothing() {

  }

  @Test
  public void throwsNullPointerException() {
    thrown.expect(NullPointerException.class);
    throw new NullPointerException();
  }

  @Test
  public void throwsNullPointerExceptionWithMessage() {
    thrown.expect(NullPointerException.class);
    thrown.expectMessage("happened?");
    thrown.expectMessage(startsWith("What"));
    throw new NullPointerException("What happened?");
  }
}
```

##### ClassRule

类似于 `aop` 在之前之后做点操作

```java
@RunWith(Suite.class)
@SuiteClasses({A.class, B.class, C.class})
public class UsesExternalResource {
  public static final Server myServer = new Server();

  @ClassRule
  public static final ExternalResource resource = new ExternalResource() {
    @Override
    protected void before() throws Throwable {
      myServer.connect();
    };

    @Override
    protected void after() {
      myServer.disconnect();
    };
  };
}
```

##### RuleChain

可以指定 rule 顺序，

> The `RuleChain` rule allows ordering of `TestRules`:

```java
public static class UseRuleChain {
    @Rule
    public final TestRule chain = RuleChain
                           .outerRule(new LoggingRule("outer rule"))
                           .around(new LoggingRule("middle rule"))
                           .around(new LoggingRule("inner rule"));

    @Test
    public void example() {
        assertTrue(true);
    }
}
```



##### Custom Rules

一般继承 `ExternalResource` 就够了。如果需要更多信息可以实现 `TestRule` 接口：

```java
public class IdentityRule implements TestRule {
  @Override
  public Statement apply(final Statement base, final Description description) {
    return base;
  }
}
```

一个例子：

```java
public class TestLogger implements TestRule {
  private Logger logger;

  public Logger getLogger() {
    return this.logger;
  }

  @Override
  public Statement apply(final Statement base, final Description description) {
    // 注意这里返回了一个新的 statement
    return new Statement() {
      @Override
      public void evaluate() throws Throwable {
        logger = Logger.getLogger(description.getTestClass().getName() + '.' + description.getDisplayName());
        base.evaluate();
      }
    };
  }
}
```

使用：

```java
public class MyLoggerTest {

  @Rule
  public final TestLogger logger = new TestLogger();

  @Test
  public void checkOutMyLogger() {
    final Logger log = logger.getLogger();
    log.warn("Your test is showing!");
  }

}
```



#### Theories

要点：

​	`@DataPoint`，必须放到 public static 属性上，不然会报错

​	`@Theory`

只有用户名不包含斜杠时才进行测试：

```java
public class UserTest {
    @DataPoint
    public static String GOOD_USERNAME = "optimus";
    @DataPoint
    public static String USERNAME_WITH_SLASH = "optimus/prime";

    // 会对所有 @datapoint 执行此方法
    @Theory
    public void filenameIncludesUsername(String username) {
        assumeThat(username, not(containsString("/")));
        assertThat(new User(username).configFileName(), containsString(username));
    }
}
```

可以用 `@TestedOn`

```java
@Theory
public void multiplyIsInverseOfDivideWithInlineDataPoints(
        @TestedOn(ints = {0, 5, 10}) int amount,
        @TestedOn(ints = {0, 1, 2}) int m
) {
    assumeThat(m, not(0));
    assertThat(new Dollar(amount).times(m).divideBy(m).getAmount(), is(amount));
}
```

也可以继承 `ParameterSupplier`：

```java
@Retention(RetentionPolicy.RUNTIME)
@ParametersSuppliedBy(BetweenSupplier.class)
public @interface Between {
    int first();

    int last();
}
```

```java
public static class BetweenSupplier extends ParameterSupplier {
    @Override
    public List getValues(Object test, ParameterSignature sig) {
        Between annotation = (Between) sig.getSupplierAnnotation();

        ArrayList list = new ArrayList();
        for (int i = annotation.first(); i <= annotation.last(); i++)
            list.add(i);
        return list;
    }
}
```

```java
@Theory
public void multiplyIsInverseOfDivideWithInlineDataPoints(
        @Between(first = -100, last = 100) int amount,
        @Between(first = -100, last = 100) int m
) {
    assumeThat(m, not(0));
    assertThat(new Dollar(amount).times(m).divideBy(m).getAmount(), is(amount));
}
```

也可以用 `@DataPoints` 注解，放到一个方法上，返回一个集合，最后来个例子：

```java
@RunWith(Theories.class)
public class JunitTest {
    
    // 必须 public static 不然报错！
    @DataPoints
    public static List<String> go() {
        return Arrays.asList("zhangsan", "lisi");
    }
    
    @Theory
    public void test(String name){
        System.out.println(name);
    }
}
```



#### Test fixtures

> The purpose of a test fixture is to ensure that there is a well known and fixed environment in which tests are run so that results are repeatable



> - Preparation of input data and setup/creation of fake or mock objects
> - Loading a database with a specific, known set of data
> - Copying a specific known set of files creating a test fixture will create a set of objects initialized to certain states（这句话看不懂）



简单来说就是四个注解：

`@BeforeClass`

`@AfterClass`

`@Before`

`@After`



#### Categories

核心

```java
@RunWith(Categories.class)
@IncludeCategory(SlowTests.class)
```

支持继承。



一个长例子说明问题：

```java
public interface FastTests { /* category marker */ }
public interface SlowTests { /* category marker */ }

public class A {
  @Test
  public void a() {
    fail();
  }

  @Category(SlowTests.class)
  @Test
  public void b() {
  }
}

@Category({SlowTests.class, FastTests.class})
public class B {
  @Test
  public void c() {

  }
}

@RunWith(Categories.class)
@IncludeCategory(SlowTests.class)
@SuiteClasses( { A.class, B.class }) // Note that Categories is a kind of Suite
public class SlowTestSuite {
  // Will run A.b and B.c, but not A.a
}

@RunWith(Categories.class)
@IncludeCategory(SlowTests.class)
@ExcludeCategory(FastTests.class)
@SuiteClasses( { A.class, B.class }) // Note that Categories is a kind of Suite
public class SlowTestSuite {
  // Will run A.b, but not A.a or B.c
}
```



*maven-surefire-plugin (for unit tests) or maven-failsafe-plugin (for integration tests*)



常见的分类：

**The type of automated tests:** 

`UnitTests, IntegrationTests, SmokeTests, RegressionTests, PerformanceTests ...`

**How quick the tests execute:** 

`SlowTests, QuickTests`

 **In which part of the ci build the tests should be executed:**

`NightlyBuildTests`

**The state of the test:** 

`UnstableTests, InProgressTests`

























