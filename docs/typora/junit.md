**JUnit 5 =** **JUnit Platform** **+** **JUnit Jupiter** **+** **JUnit Vintage**

JUnit Platform 用来启动测试框架

JUnit Jupiter 提供了新的编程模型和扩展模型

JUnit Vintage 为了兼容运行旧的 junit 版本，junit3 和 junit 4

JUnit 5 至少需要 java8 来运行



#### 概念

**Test Class** <font color='red'>顶级类</font>、静态内部类和 有 `@Nested` 注解的非静态内部类（必须有至少一个 `@Test` 的方法）

**Test Method** 任何实例方法，有这些注解： `@Test, @RepeatedTest, @ParameterizedTest, @TestFactory, or @TestTemplate`

**Lifecycle Method** 有这些注解的方法：`@BeforeAll,@AfterAll, @BeforeEach, or @AfterEach`

*注意事项：* 方法一定是 void 类型；它们不一定必须 public 但必须不是 private；Lifecycle Method 可以继承自接口或父类。



##### **Display Name**

`@DisplayName` 可放到类上或方法上，不加被测试内容显示为类名、方法名

`@DisplayNameGeneration` 传进去一个 `DisplayNameGenerator` 类型；它没前者优先级高，只能作用在类上

`src/test/resources/junit-platform.properties` 配置

```properties
junit.jupiter.displayname.generator.default = \
  org.junit.jupiter.api.DisplayNameGenerator$ReplaceUnderscores
```

可以设置默认的 displayname generator

*还有第四种*  `by calling org.junit.jupiter.api.DisplayNameGenerator.Standard` 不想试了累了

*新单词*  `lend themselves well to being` 很适合做

#### Assertions

*jupiter 也有个 assertions，简单说一说*



assertAll 里的每个 lambda 都会执行

```java
  @Test
  void groupedAssertions() {
  // In a grouped assertion all assertions are executed, and all
  // failures will be reported together.
  assertAll("person",
  () -> assertEquals("Jane", person.getFirstName()),
  () -> assertEquals("Doe", person.getLastName())
  );
  }
```

*超时断言*

```java
  @Test
  void timeoutNotExceeded() {
  // The following assertion succeeds.
  assertTimeout(ofMinutes(2), () -> {
  // Perform task that takes less than 2 minutes.
  });
  }
```

*超时断言可以有个返回值*

```java
  @Test
  void timeoutNotExceededWithResult() {
  // The following assertion succeeds, and returns the supplied object.
  String actualResult = assertTimeout(ofMinutes(2), () -> {
  return "a result";
  });
  assertEquals("a result", actualResult);
  }
```

*不等任务执行完*：如果待测试内容依赖 threadlocal，那完了，因为这个测试在另一个线程执行。

```java
  @Test
  void timeoutExceededWithPreemptiveTermination() {
  // The following assertion fails with an error message similar to:
  // execution timed out after 10 ms
  assertTimeoutPreemptively(ofMillis(10), () -> {
  // Simulate task that takes more than 10 ms.
  new CountDownLatch(1).await();
  });
  }
```



#### Assumptions

有类似于 Assertions 的 `assumeTrue` 这种，如果 assume 的内容失败了，抛出 `org.opentest4j.TestAbortedException` 标记测试被跳过，也可以识别 junit4 的 `AssumptionViolatedException` 这样测试被跳过而不是标记为失败

还有一种 `assumingThat(false, () -> {});` 如果前面是 true 就执行后面的 lambda，是 false 也不影响后续代码执行

#### 忽略一个测试

方法一  @Disabled，可以放到 类 或 方法上，可以写一个简短描述（推荐）

方法二 使用 Conditional Test Execution

`org.junit.jupiter.api.condition` 包里有一些条件注解，不满足的测试被跳过

都是自描述注解，无需解释

`@EnabledOnOs`

 `@DisabledOnOs ` 

`@EnabledOnJre(JAVA_8)` 

`@EnabledForJreRange(min = JAVA_9, max = JAVA_11)` 

// 下面这个 matches 是个正则表达式字符串

// 5.6 起它是可重复注解了

`@EnabledIfSystemProperty(named = "os.arch", matches = ".*64.*")`

下面这俩和上面那个类似

`@EnabledIfEnvironmentVariable `

`@DisabledIfEnvironmentVariable `

方法三 通过 ExecutionCondition



#### @Tag

tag 里不能有空格或控制字符或 `, ( ) & | !` 中的任何一个



#### **Test Execution Order**

何时需要考虑测试顺序？

*for example, when writing integration tests or functional tests where the sequence of the tests is important, especially in conjunction with `@TestInstance(Lifecycle.PER_CLASS)`*

怎么制定顺序

加注解 `@TestMethodOrder ` 指定 `MethodOrderer` 实现类，放到 **类**上。有三个内置实现类

`Alphanumeric`

`OrderAnnotation`，测试方法上配合 `@Order`注解

`Random`

####  **Test Instance Lifecycle**

*In order to allow individual test methods to be executed in isolation and to avoid unexpected side effects due to mutable test instance state, JUnit creates a new instance of each test class before executing each test method*

为了更好地隔离，每个测试方法都 new 一个新测试类实例，被通过注解忽略的方法也会导致 new 新实例。

要修改这一行为，可以往测试类上加注解 `@TestInstance(Lifecycle.PER_CLASS)`，这样导致每个测试类只实例化一次，不过内部状态需要通过 `@BeforeEach` 或 `@AfterEach` 来修改

per class 的优势：`@BeforeAll` 和 `@AfterAll` 可以放到实例方法和 @Nested 类里了。

#####  **Changing the Default Test Instance Lifecycle**

1. 直接在类上加注解  `@TestInstance`
2. 系统属性 -Djunit.jupiter.testinstance.lifecycle.default=per_class
3. 配置文件 `junit.jupiter.testinstance.lifecycle.default = per_class`
4. 还有个办法，`as a *configuration parameter* in the LauncherDiscoveryRequest that is passed to the Launcher`，<u>不知道啥意思</u>

#### **Nested Tests**

*`@Nested` tests give the test writer more capabilities to express the relationship among several groups of tests. Here’s an elaborate example*

很好的例子，

```java
@DisplayName("A stack")
class TestingAStackDemo {
  Stack<Object> stack;
    
  @Test
  @DisplayName("is instantiated with new Stack()")
  void isInstantiatedWithNew() {
  	new Stack<>();
  }
    
  @Nested
  @DisplayName("when new")
  class WhenNew {
      @BeforeEach
      void createNewStack() {
      	stack = new Stack<>();
      }
      
      @Test
      @DisplayName("is empty")
      void isEmpty() {
      	assertTrue(stack.isEmpty());
      }
      
      @Test
      @DisplayName("throws EmptyStackException when popped")
      void throwsExceptionWhenPopped() {
     	 assertThrows(EmptyStackException.class, stack::pop);
      }
      
      @Test
      @DisplayName("throws EmptyStackException when peeked")
      void throwsExceptionWhenPeeked() {
      	assertThrows(EmptyStackException.class, stack::peek);
      }
      
      @Nested
      @DisplayName("after pushing an element")
      class AfterPushing {
          String anElement = "an element";
          
          @BeforeEach
          void pushAnElement() {
          	stack.push(anElement);
          }
          
          @Test
          @DisplayName("it is no longer empty")
          void isNotEmpty() {
              assertFalse(stack.isEmpty());
          }
          
          @Test
          @DisplayName("returns the element when popped and is empty")
          void returnElementWhenPopped() {
              assertEquals(anElement, stack.pop());
              assertTrue(stack.isEmpty());
          }
          
          @Test
          @DisplayName("returns the element when peeked but remains not empty")
          void returnElementWhenPeeked() {
              assertEquals(anElement, stack.peek());
              assertFalse(stack.isEmpty());
          }
      }
  } 
}
```

默认情况下`@Nested`类不能有 `@BeforeAll` `@AfterAll` 因为它不能有静态成员但是 `@TestInstance(Lifecycle.PER_CLASS)` 可以解决这个问题

#### **Dependency Injection for Constructors and Methods**

junit 现在可以依赖注入，是靠 `ParameterResolver` 实现的，默认 `ParameterResolver` 有：

- `TestInfoParameterResolver` 可以帮你注入 TestInfo，可以获取 displayName，tag 之类的东西，在 junit4 中

有个 `TestName` rule 也可以实现这个功能

- `RepetitionInfoParameterResolver` 当一个测试方法是 `@RepeatedTest` 时它的 `RepetitionInfo`

参数可以被注入。

- `TestReporterParameterResolver` 构造或方法中的 TestReporter 参数会被注入；`TestExecutionListener` 的 `reportingEntryPublished()` 方法会消费这个参数的内容

在 `junit jupiter` 里东西应该使用 TestReporter 而不是 stdout 或 stderr 来打印

```java
  @Test
  void reportSingleValue(TestReporter testReporter) {
  	testReporter.publishEntry("a status message");
  }
```

`RunWith(JUnitPlatform.class)` 会把所有报告内容打印到 stdout



- 其他类型的参数解析器（除了上面那三个）需要使用注解` @ExtendWith` 来显式注册才会生效

来实现一个自定义 ParameterResolver：

```java
// 也不难理解，不废话了
public class RandomParametersExtension implements ParameterResolver {

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.PARAMETER)
	public @interface Random {
	}

	@Override
	public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
		return parameterContext.isAnnotated(Random.class);
	}

	@Override
	public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
		return getRandomValue(parameterContext.getParameter(), extensionContext);
	}

	private Object getRandomValue(Parameter parameter, ExtensionContext extensionContext) {
		Class<?> type = parameter.getType();
		java.util.Random random = extensionContext.getRoot().getStore(Namespace.GLOBAL)//
				.getOrComputeIfAbsent(java.util.Random.class);
		if (int.class.equals(type)) {
			return random.nextInt();
		}
		if (double.class.equals(type)) {
			return random.nextDouble();
		}
		throw new ParameterResolutionException("No random generator implemented for " + type);
	}

}
```

下面是它的用法：

```java
// 很优雅
@ExtendWith(RandomParametersExtension.class)
class MyRandomParametersTest {
  @Test
  void injectsInteger(@Random int i, @Random int j) {
  	assertNotEquals(i, j);
  }
  @Test
  void injectsDouble(@Random double d) {
  	assertEquals(0.0, d, 1.0);
  } 
}
```

#### **Test Interfaces and Default Methods**

这些注解可以放到 defaut 方法上：

```java
@Test
@RepeatedTest
@ParameterizedTest
@TestFactory
@TestTemplate
@BeforeEach
@AfterEach
```

如果测试类上有注解 `@TestInstance(Lifecycle.PER_CLASS) ` 意思是测试类只实例化一次，这样

```java
@BeforeAll
@AfterAll
```

也可以放到 default 方法上，否则它俩只能放到静态方法上



 一个 @TestFacotry 和 dynamicTest 例子，目前不懂

```java
interface TestInterfaceDynamicTestsDemo {
    
  @TestFactory
  default Stream<DynamicTest> dynamicTestsForPalindromes() {
  return Stream.of("racecar", "radar", "mom", "dad")
  .map(text -> dynamicTest(text, () -> assertTrue(isPalindrome(text))));
  } 
}
```



`@ExtendWith` `@Tag` 可以被继承

> @ExtendWith and @Tag can be declared on a test interface so that classes that implement the interface automatically inherit its tags and extensions



#### **Repeated Tests**

`@RepeatedTest(10)` 执行十次，次注解有个 name 属性，可以使用变量，目前只支持三个：

> {displayName}: display name of the `@RepeatedTest` method
>
> {currentRepetition}: the current repetition count
>
> {totalRepetitions}: the total number of repetitions

它默认格式是：`repetition {currentRepetition} of {totalRepetitions}`，是这个效果：

`repetition 1 of 10, repetition 2 of 10`

有个常量 `RepeatedTest.LONG_DISPLAY_NAME` 它内容是 `"{displayName} :: repetition

{currentRepetition} of {totalRepetitions}` 也可以直接用。

也可以通过注入  RepetitionInfo 来获取这些玩意儿。



#### 重头戏：Parameterized Tests

必须带一个 source 注解提供参数源

```java
@ParameterizedTest
@ValueSource(strings = { "racecar", "radar", "able was I ere I saw elba" })
void palindromes(String candidate) {
  assertTrue(StringUtils.isPalindrome(candidate)); 
}
```

一般源和参数位置是一对一的，但也可以聚合成一个对象传给测试方法。

到目前为止，测试方法可有三种注入：

- 按位置参数注入，由 `ArgumentsProvider` 提供，必须放到最一开始
- 参数聚合后注入，有`ArgumentsAccessor` 提供，必须放到中间，可能带有 @AggregateWith 注解
- ParameterProvider 注入的参数必须放到最后

######  **Sources of Arguments**

`org.junit.jupiter.params.provider` 包里有一些内置的参数源。

`@ValueSource` 最简单的一种，支持 8 种基础类型，String 和 Class

`@NullSource` 测 corner case 的，提供一个 null

` @EmptySource` 目前支持 `String List Set Map int[] int[][] 对象数组` 等空对象（不是null，是空）

`@NullAndEmptySource` 前两者的混合

```java
// 测试特殊输入
@ParameterizedTest
@NullSource
@EmptySource
@ValueSource(strings = { " ", " ", "\t", "\n" })
void nullEmptyAndBlankStrings(String text) {
  assertTrue(text == null || text.trim().isEmpty()); 
}
```

`@EnumSource` 很显然注入一个枚举的所有实例。

它比较特殊，可以不带参数，但是这样的话测试方法第一个参数必须是个枚举类型，会被自动注入，利用这一点，有下面的例子：

```java
// 设置模式，排除一些枚举常量
@ParameterizedTest
@EnumSource(mode = EXCLUDE, names = { "ERAS", "FOREVER" })
void testWithEnumSourceExclude(ChronoUnit unit) {
  assertFalse(EnumSet.of(ChronoUnit.ERAS, ChronoUnit.FOREVER).contains(unit)); 
}

// 使用正则匹配举常量，注意模式
@ParameterizedTest
@EnumSource(mode = MATCH_ALL, names = "^.*DAYS$")
void testWithEnumSourceRegex(ChronoUnit unit) {
  assertTrue(unit.name().endsWith("DAYS")); }
```



`@MethodSource` 允许引用工厂方法

工厂方法：必须不能有参数；一般必须是 static （除非测试类有注解 `@TestInstance(Lifecycle.PerClass)`）

对于只有一个参数的测试方法，返回普通的集合或流类型即可，也可以返回参数是 Argument 的集合或流类型；

对于需要多个参数的测试方法，则需要返回参数是 Arguments 的集合或流类型:

单参：

```java
// 故意选了个特例：如果没指定方法名，则从当前类找同名、无参的方法作为工厂方法
@ParameterizedTest
@MethodSource
void testWithDefaultLocalMethodSource(String argument) {
  assertNotNull(argument); 
}

static Stream<String> testWithDefaultLocalMethodSource() {
  return Stream.of("apple", "banana"); 
}
```

多参：

```java
@ParameterizedTest
@MethodSource("stringIntAndListProvider")
void testWithMultiArgMethodSource(String str, int num, List<String> list) {
  assertEquals(5, str.length());
  assertTrue(num >=1 && num <=2);
  assertEquals(2, list.size()); 

}
static Stream<Arguments> stringIntAndListProvider() {
  return Stream.of(
  arguments("apple", 1, Arrays.asList("a", "b")),
  arguments("lemon", 2, Arrays.asList("x", "y"))
  ); 
}
```

如果工厂方法在别的类里：

```java
class ExternalMethodSourceDemo {
  @ParameterizedTest
  // 注意这个参数的写法
  @MethodSource("example.StringsProviders#tinyStrings")
  void testWithExternalMethodSource(String tinyString) {
  	// test with tiny string
  } 
}
  
class StringsProviders {
  static Stream<String> tinyStrings() {
  	return Stream.of(".", "oo", "OOO");
  } 
}
```

`@CsvSource`

```java
// 看来自带类型
@ParameterizedTest
@CsvSource({
  "apple, 1",
  "banana, 2",
  "'lemon, lime', 0xF1" })
void testWithCsvSource(String fruit, int rank) {
  assertNotNull(fruit);
  assertNotEquals(0, rank); 
}
```

> The default delimiter is a comma (,), but you can use another character by setting the delimiter attribute. 
>
> Alternatively, the delimiterString attribute allows you to use a String delimiter instead of a single character.
>
>  However, both delimiter attributes cannot be set simultaneously.

使用单引号括起来，空单引号 `''` 默认是 空，除非指定 emtyValue

逗号后面没值默认是 null

可以指定 nullValue 属性，告诉它什么情况下是 null（比如导出的数据 N/A 可以认为是 null）

|                                                              |                         |
| ------------------------------------------------------------ | ----------------------- |
| @CsvSource({ "apple, banana" })                              | "apple", "banana"       |
| @CsvSource({ "apple, 'lemon, lime'" })                       | "apple", "lemon, lime"  |
| @CsvSource({ "apple, " })                                    | "apple", null           |
| @CsvSource(value = { "apple, banana, NIL" }, nullValues = "NIL") | "apple", "banana", null |

`@CsvFileSource`

它使用双引号括起来字符串类型数据，和 @CsvSource 不同。

> Any line beginning with a # symbol will be interpreted as a comment and will be ignored

一行调用一次测试方法

```java
@ParameterizedTest
// 设置跳过一行
@CsvFileSource(resources = "/two-column.csv", numLinesToSkip = 1)
void testWithCsvFileSource(String country, int reference) {
  assertNotNull(country);
  assertNotEquals(0, reference); 
}
```

`@ArgumentsSource`

需要提供 `ArgumentsProvider` 参数，需要是 static 内部类或外部类

```java
@ParameterizedTest
@ArgumentsSource(MyArgumentsProvider.class)
void testWithArgumentsSource(String argument) {
  assertNotNull(argument); 
}

public class MyArgumentsProvider implements ArgumentsProvider {
  @Override
  public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
  return Stream.of("apple", "banana").map(Arguments::of);
  } 
}
```

#####  Argument Conversion

> JUnit Jupiter supports Widening Primitive Conversion for arguments supplied to a `@ParameterizedTest`

还提供了很多 String -> 类型的转换：

有`基础类型、class、time、File、URL、Charset、UUID`

*Fallback String-to-Object Conversion* 如果一个类有只接收 String 类型的静态工厂方法或构造，可以隐式构造这个类的实例；前者优先级高于后者；如果发现多个静态工厂，则不会构造

```java
@ParameterizedTest
@ValueSource(strings = "42 Cats")
void testWithImplicitFallbackArgumentConversion(Book book) {
  assertEquals("42 Cats", book.getTitle()); 
}

public class Book {
  private final String title;
  private Book(String title) {
  	this.title = title;
  }
  public static Book fromTitle(String title) {
  	return new Book(title);
  }
}
```

也可以显式指定一个转换器

```java
@ParameterizedTest
@EnumSource(ChronoUnit.class)
void testWithExplicitArgumentConversion(
  @ConvertWith(ToStringArgumentConverter.class) String argument) {
  assertNotNull(ChronoUnit.valueOf(argument)); 
}

public class ToStringArgumentConverter extends SimpleArgumentConverter {
  @Override
  protected Object convert(Object source, Class<?> targetType) {
      assertEquals(String.class, targetType, "Can only convert to String");
      if (source instanceof Enum<?>) {
          return ((Enum<?>) source).name();
      }
      return String.valueOf(source);
  } 
}
```

jupiter 只提供了一个显式的参数转换器，使用复合注解 `@JavaTimeConversionPattern` 来实现

```java
@ParameterizedTest
@ValueSource(strings = { "01.01.2017", "31.12.2017" })
void testWithExplicitJavaTimeConverter(
  @JavaTimeConversionPattern("dd.MM.yyyy") LocalDate argument) {
  assertEquals(2017, argument.getYear()); 
}
```

#### Argument Aggregation

- 如果参数多，可以使用自动注入的 `ArgumentsAccessor`，看个例子就明白了了

```java
@ParameterizedTest
@CsvSource({
  "Jane, Doe, F, 1990-05-20",
  "John, Doe, M, 1990-10-22" })
void testWithArgumentsAccessor(ArgumentsAccessor arguments) {
  Person person = new Person(arguments.getString(0),
                             arguments.getString(1),
                             arguments.get(2, Gender.class),
                             arguments.get(3, LocalDate.class));
    
  if (person.getFirstName().equals("Jane")) {
  	assertEquals(Gender.F, person.getGender());
  }
  else {
  	assertEquals(Gender.M, person.getGender());
  }
  assertEquals("Doe", person.getLastName());
  assertEquals(1990, person.getDateOfBirth().getYear()); 
}
```

- 下面这个方法稍微好一点，通过实现 `ArgumentsAggregator` 接口和使用 `@AggregateWith` 注解来实现：

```java
@ParameterizedTest
@CsvSource({
  "Jane, Doe, F, 1990-05-20",
  "John, Doe, M, 1990-10-22" })
void testWithArgumentsAggregator(@AggregateWith(PersonAggregator.class) Person person) {
  // perform assertions against person
}

public class PersonAggregator implements ArgumentsAggregator {
  @Override
  public Person aggregateArguments(ArgumentsAccessor arguments, ParameterContext
context) {
      return new Person(arguments.getString(0),
                        arguments.getString(1),
                        arguments.get(2, Gender.class),
                        arguments.get(3, LocalDate.class));
  } 
}
```

- 如果上面这种方法用的很多，还有个办法可以减少代码量，利用元注解：

```java
@ParameterizedTest
@CsvSource({
  "Jane, Doe, F, 1990-05-20",
  "John, Doe, M, 1990-10-22" })
void testWithCustomAggregatorAnnotation(@CsvToPerson Person person) {
  // perform assertions against person
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@AggregateWith(PersonAggregator.class)
public @interface CsvToPerson {}
```

`@ParameterizedTest` name 属性里的变量：

有这么几个内置 placeholder：

| placeholder          | description                               |
| -------------------- | ----------------------------------------- |
| {displayName}        | 方法 displayname                          |
| {index}              | 当前传入的参数们是参数源的第几个？1-based |
| {arguments}          | 逗号隔开的参数列表                        |
| {argumentsWithNames} | 逗号隔开参数列表，带上参数名              |
| {0}, {1}, ...        | 每个参数                                  |



#### Test Templates

@TestTemplate 不是正经测试，需要注册 TestTemplateInvocationContextProvider ，涉及到一些高级技巧，后面说

> Repeated Tests and Parameterized Tests are built-in specializations of test templates



#### Dynamic Tests

> In contrast to `@Test` methods, a `@TestFactory` method is not itself a test case but rather a factory for test cases. Thus, a dynamic test is the product of a factory. Technically speaking, a `@TestFactory` method must return a single **`DynamicNode`** or a **Stream, Collection, Iterable, Iterator, or array** of **`DynamicNode`** instances.

意思是这个工厂返回一个或多个 `DynamicNode`.

这个工厂方法一次调用可以返回多个测试，但是只有这个工厂方法享受生命周期，生成的测试不能享受。

来一个有些长，但是十分清晰的例子，

```java
import static example.util.StringUtils.isPalindrome;
import static org.junit.jupiter.api.Assertions.assertEquals;
61
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.DynamicContainer.dynamicContainer;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import example.util.Calculator;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.function.ThrowingConsumer;

class DynamicTestsDemo {
  private final Calculator calculator = new Calculator();
    
  // This will result in a JUnitException!
  @TestFactory
  List<String> dynamicTestsWithInvalidReturnType() {
  	return Arrays.asList("Hello");
  }
   
   // 第一个参数，是 displayname，后面这个 consumer，就相当于测试方法
  @TestFactory
  Collection<DynamicTest> dynamicTestsFromCollection() {
      return Arrays.asList(
          dynamicTest("1st dynamic test", 
                      () -> assertTrue(isPalindrome("madam"))),
          dynamicTest("2nd dynamic test", 
                      () -> assertEquals(4, calculator.multiply(2, 2)))
      );
  }
    
  @TestFactory
  Iterable<DynamicTest> dynamicTestsFromIterable() {
      return Arrays.asList(
          dynamicTest("3rd dynamic test", () -> assertTrue(isPalindrome("madam"))),
          dynamicTest("4th dynamic test", 
                      () -> assertEquals(4, calculator.multiply(2, 2)))
      );
  }

  @TestFactory
  Iterator<DynamicTest> dynamicTestsFromIterator() {
      return Arrays.asList(
          dynamicTest("5th dynamic test", 
                      () -> assertTrue(isPalindrome("madam"))),
          dynamicTest("6th dynamic test", 
                      () -> assertEquals(4, calculator.multiply(2, 2)))
      ).iterator();
  }
    
  @TestFactory
  DynamicTest[] dynamicTestsFromArray() {
      return new DynamicTest[] {
          dynamicTest("7th dynamic test", 
                      () -> assertTrue(isPalindrome("madam"))),
          dynamicTest("8th dynamic test", 
                      () -> assertEquals(4, calculator.multiply(2, 2)))
      };
  }
    
  @TestFactory
  Stream<DynamicTest> dynamicTestsFromStream() {
      return Stream.of("racecar", "radar", "mom", "dad")
      .map(text -> dynamicTest(text, () -> assertTrue(isPalindrome(text))));
  }
    
  @TestFactory
  Stream<DynamicTest> dynamicTestsFromIntStream() {
      // Generates tests for the first 10 even integers.
      return IntStream.iterate(0, n -> n + 2).limit(10)
          .mapToObj(n -> dynamicTest("test" + n, () -> assertTrue(n % 2 == 0)));
  }
    
  @TestFactory
  Stream<DynamicTest> generateRandomNumberOfTests() {
      // Generates random positive integers between 0 and 100 until
      // a number evenly divisible by 7 is encountered.
      Iterator<Integer> inputGenerator = new Iterator<Integer>() {
          Random random = new Random();
          int current;
          @Override
          public boolean hasNext() {
              current = random.nextInt(100);
              return current % 7 != 0;
          }
          @Override
          public Integer next() {
              return current;
          }
      };
      
      // Generates display names like: input:5, input:37, input:85, etc.
      Function<Integer, String> displayNameGenerator = (input) -> "input:" + input;
      // Executes tests based on the current input value.
      ThrowingConsumer<Integer> testExecutor = (input) -> assertTrue(input % 7 != 0 );
      // Returns a stream of dynamic tests.
      return DynamicTest.stream(inputGenerator, displayNameGenerator, testExecutor);
	}
    
    // 这个比较长，光整理括号整理了十几分钟
    // 意思就是 dynamicContainer 第二个参数可以是 DynamicNode 的流，支持嵌套
    // 鬼才这么写，没法看
    @TestFactory
    Stream<DynamicNode> dynamicTestsWithContainers() {
      return Stream.of("A", "B", "C")
          .map(input -> 
               dynamicContainer("Container " + input, 
              					Stream.of(dynamicTest("not null",() -> assertNotNull(input)),
              dynamicContainer("properties", Stream.of(
                      dynamicTest("length > 0", () -> assertTrue(input.length() > 0)),
                      dynamicTest("not empty", () -> assertFalse(input.isEmpty())))
              ))));
  	}
    
  @TestFactory
  DynamicNode dynamicNodeSingleTest() {
  	return dynamicTest("'pop' is a palindrome", () -> assertTrue(isPalindrome("pop")));
  }
                                                                            
  @TestFactory
  DynamicNode dynamicNodeSingleContainer() {
      return dynamicContainer("palindromes", 
                              Stream.of("racecar", "radar", "mom", "dad")
      								.map(text -> dynamicTest(text, () -> 															assertTrue(isPalindrome(text))
                                         )
                              )
      		);
  } 
}
```



*URI Test Sources for Dynamic Tests* 这一段没看懂是干啥的，传了一个 URI，扩展了一些东西。



#### Timeouts

`@Timeout`注解加载一个方法上，这个方法在当前线程执行，到时间后，另一个线程会 interrupt 此线程。

可以加在类上，所有方法都有时间限制了，但是不影响生命周期方法。

加在 @TestFactory 上的 `@Timeout`，只影响生成测试的方法，不影响生成的测试。

加载 @TestTemplate 上的，影响每次调用。如 @ParamterizedTest

value 可以带单位，默认是秒

`42 ns 相当于 @Timeout(value = 42, unit = NANOSECONDS)`

有时 debug 时会导致超时，可以全局设置超时模式：

`junit.jupiter.execution.timeout.mode: enabled, disabled, and disabled_on_debug`

`disabled_on_debug` 发现 -agentlib:jdwp 时认为是 debug mode



#### Parallel Execution

设置并行测试第一步：

`junit.jupiter.execution.parallel.enabled=true`

第二步：

` junit.jupiter.execution.parallel.mode.default=concurrent`

他有两个 mode，**SAME_THREAD** 和 **CONCURRENT**，默认是前者

或通过  @Execution 设置 mode

全局设置对两种情况不适用：@TestInstance(LifeCyle.PER_CLASS) 和制定了 methodOrder 的，这两种情况下只有使用注解方式来并行。

```properties
// Configuration parameters to execute top-level classes in parallel but methods in same // thread
junit.jupiter.execution.parallel.enabled = true
junit.jupiter.execution.parallel.mode.default = same_thread
junit.jupiter.execution.parallel.mode.classes.default = concurrent
```

```properties
// Configuration parameters to execute top-level classes in sequentially but their 
// methods in parallel
junit.jupiter.execution.parallel.enabled = true
junit.jupiter.execution.parallel.mode.default = concurrent
junit.jupiter.execution.parallel.mode.classes.default = same_thread
```

> If the junit.jupiter.execution.parallel.mode.classes.default configuration parameter is not explicitly set, the value for junit.jupiter.execution.parallel.mode.default will be used instead

你可以显式指定类之间怎么并行、类的方法之间怎么并行，不过如果没指定前者，前者使用后者的值

`junit.jupiter.execution.parallel.config.strategy` 指定并行策略，分`dynamic` 和 `fixed`和  `custom`

- `dynamic` 默认。核数乘以 `junit.jupiter.execution.parallel.config.dynamic.factor` 值，默认1

- `fixed`  `junit.jupiter.execution.parallel.config.fixed.parallelism` 指定

- `custom` `junit.jupiter.execution.parallel.config.custom.class ` 指定，实现它`ParallelExecutionConfigurationStrategy ` 

  

*资源锁定 `@ResourceLock `，传入一个字符串，相当于一个锁，有几个内置的字符串：*

`Resources: SYSTEM_PROPERTIES, SYSTEM_OUT, SYSTEM_ERR, LOCALE, or TIME_ZONE.`

不锁的话，可能有些测试改了资源导致其它测试失败。

还可以指定 mode，READ/READ_WIRTE，都带着 READ 的可以并行

```java
@Execution(CONCURRENT)
class SharedResourcesDemo {
  private Properties backup;
  @BeforeEach
  void backup() {
      backup = new Properties();
      backup.putAll(System.getProperties());
  }
    
  @AfterEach
  void restore() {
  	System.setProperties(backup);
  }
    
  @Test
  @ResourceLock(value = SYSTEM_PROPERTIES, mode = READ)
  void customPropertyIsNotSetByDefault() {
  	assertNull(System.getProperty("my.prop"));
  }
    
  @Test
  @ResourceLock(value = SYSTEM_PROPERTIES, mode = READ_WRITE)
  void canSetCustomPropertyToApple() {
      System.setProperty("my.prop", "apple");
      assertEquals("apple", System.getProperty("my.prop"));
  }
    
  @Test
  @ResourceLock(value = SYSTEM_PROPERTIES, mode = READ_WRITE)
  void canSetCustomPropertyToBanana() {
      System.setProperty("my.prop", "banana");
      assertEquals("banana", System.getProperty("my.prop"));
  } 
}
```



#### Built-in Extensions

目前就一个 `@TempDir`，可以加在**非 private 属性**上或**非构造方法参数**里；

类型可以是 `java.nio.file.Path or java.io.File`

```java
@Test
void writeItemsToFile(@TempDir Path tempDir) throws IOException {
  Path file = tempDir.resolve("test.txt");
  new ListWriter(file).write("a", "b", "c");
  assertEquals(singletonList("a,b,c"), Files.readAllLines(file)); 
}

class SharedTempDirectoryDemo {
  // 不是静态的话每个 test 都创建一个临时目录
  @TempDir
  static Path sharedTempDir;
    
  @Test
  void writeItemsToFile() throws IOException {
      Path file = sharedTempDir.resolve("test.txt");
      new ListWriter(file).write("a", "b", "c");
      assertEquals(singletonList("a,b,c"), Files.readAllLines(file));
  }
    
  @Test
  void anotherTestThatUsesTheSameTempDir() {
  	// use sharedTempDir
  } 
}
```



#### 和 4 比，变化（关键！）

>  Annotations reside in the `org.junit.jupiter.api` package.
>
> Assertions reside in `org.junit.jupiter.api.Assertions`. 
>
> Note that you may continue to use assertion methods from `org.junit.Assert` or any other
>
> assertion library such as `AssertJ`, `Hamcrest`, `Truth`, etc.
>
> Assumptions reside in `org.junit.jupiter.api.Assumptions`. 
>
> Note that JUnit Jupiter 5.4 and later versions support methods from JUnit 4’s `org.junit.Assume` class for assumptions. Specifically, JUnit Jupiter supports JUnit 4’s `AssumptionViolatedException` to signal that a test should be aborted instead of marked as a failure.
>
> `@Before` and `@After` no longer exist; use `@BeforeEach` and `@AfterEach` instead.
>
> `@BeforeClass` and `@AfterClass` no longer exist; use `@BeforeAll` and `@AfterAll` instead.
>
> `@Ignore` no longer exists: use `@Disabled` or one of the other built-in execution conditions instead
>
> `@Category` no longer exists; use `@Tag` instead.
>
> `@RunWith` no longer exists; superseded by `@ExtendWith`. 
>
> `@Rule` and `@ClassRule` no longer exist; superseded by `@ExtendWith` and `@RegisterExtension`



#### Extension Model

三种注册 extention 方式

1. `@ExtendWith` 可以放在类上，方法上，可以有多个，顺序按照声明顺序生效

2. `@RegisterExtension`

   `@RegisterExtension` fields must not be private or null (at evaluation time) but may

   be either static or non-static

   把一个非空的属性注册成 extension

   静态 extension field 是类级别的，可以被全局生命周期方法访问

   ```java
   class WebServerDemo {
     @RegisterExtension
     static WebServerExtension server = WebServerExtension.builder()
                                        .enableSecurity(false)
                                        .build();
     @Test
     void getProductList() {
         WebClient webClient = new WebClient();
         String serverUrl = server.getServerUrl();
         // Use WebClient to connect to web server using serverUrl and verify response
         assertEquals(200, webClient.get(serverUrl + "/products").getResponseStatus());
     } 
   }
   ```

   实例级别的 extenstion field 会在测试方法的 `@ExtendWith` 被注册完之后才开始注册。

   如果类上有注解 `@TestInstance(Lifecycle.PER_CLASS) `，那实例级别 extenstion field 会先于测试方法级别的 `@ExtendWith` 注册。

   

   > The configured `DocumentationExtension` will be automatically registered as an extension at the method level. In addition, `@BeforeEach`, `@AfterEach`, and `@Test` methods can access the instance of the extension via the docs field if necessary

   ```java
   class DocumentationDemo {
     static Path lookUpDocsDir() {
     // return path to docs dir
     }
     
     @RegisterExtension
     DocumentationExtension docs = DocumentationExtension.forPath(lookUpDocsDir());
     
     @Test
     void generateDocumentation() {
     // use this.docs ...
     } 
   }
   ```

   

3.  `ServiceLoader`

**必须用参数** `junit.jupiter.extensions.autodetection.enabled=true` **开启**



关闭所有 CoditionExtension，需要用到配置类似于：

`junit.jupiter.conditions.deactivate=org.junit.*DisabledCondition`，使用 `*`关掉所有条件判断。

> If the `junit.jupiter.conditions.deactivate pattern` consists solely of an asterisk (*), all conditions will be deactivated

这个属性不是正则，用的是简单通配符，点就是点，星代表多个字符，是完整匹配，不是包含关系。



`TestInstancePostProcessor` 用来依赖注入，他让参考 `MockitoExtension ` 和 `SpringExtension`



`ParameterResolver extension`

jdk9 之前，内部类 java.lang.reflect.Parameter api 有bug，推荐用 

```java
boolean isAnnotated(Class<? extends Annotation> annotationType)
• Optional<A> findAnnotation(Class<A> annotationType)
• List<A> findRepeatableAnnotations(Class<A> annotationType)
```

这三个 api 来判断怎么注入参数



几个生命周期 callback：

```java
BeforeAllCallback
    BeforeEachCallback
        BeforeTestExecutionCallback
        AfterTestExecutionCallback
    AfterEachCallback
AfterAllCallback
```

他推荐看 `SpringExtension `



扩展执行顺序：怎么是倒着的，和我想的相反

```java
@ExtendWith(ThirdExecutedHandler.class)
class MultipleHandlersTestCase {
  
  @ExtendWith(SecondExecutedHandler.class)
  @ExtendWith(FirstExecutedHandler.class)
  @Test
  void testMethod() {
  } 
}
```

`InvocationInterceptor`

一个拦截器，下面例子让所有方法在一个特定线程执行：

```java
public class SwingEdtInterceptor implements InvocationInterceptor {
  @Override
  public void interceptTestMethod(Invocation<Void> invocation,
      							ReflectiveInvocationContext<Method> invocationContext,
      							ExtensionContext extensionContext) throws Throwable {
      AtomicReference<Throwable> throwable = new AtomicReference<>();
      SwingUtilities.invokeAndWait(() -> {
          try {
            invocation.proceed();
          }
          catch (Throwable t) {
            throwable.set(t);
          }
      });
      Throwable t = throwable.get();
      if (t != null) {
      	throw t;
      }
  } 
}
```



**`@TestTemplate` 怎么用**

有这个注解，再需要 `@ExtendWith` `TestTemplateInvocationContextProvider `

后面这个东西，需要返回一个 `TestTemplateInvocationContext` 的 stream，这个 context 就是每次方法执行的上下文，可以决定它有哪些扩展，之类的，看例子：

```java
final List<String> fruits=Arrays.asList("apple","banana","lemon");

@TestTemplate
@ExtendWith(MyTestTemplateInvocationContextProvider.class)
void testTemplate(String fruit){
    assertTrue(fruits.contains(fruit));
    }

public class MyTestTemplateInvocationContextProvider
    implements TestTemplateInvocationContextProvider {
    @Override
    public boolean supportsTestTemplate(ExtensionContext context) {
        return true;
    }
    
    @Override
    public Stream<TestTemplateInvocationContext>
    provideTestTemplateInvocationContexts(ExtensionContext context) {
        return Stream.of(invocationContext("apple"), invocationContext("banana"));
    }
    
    private TestTemplateInvocationContext invocationContext(String parameter) {
        return new TestTemplateInvocationContext() {
            @Override
            public String getDisplayName(int invocationIndex) {
                return parameter;
            }
            
            @Override
            public List<Extension> getAdditionalExtensions() {
                return Collections.singletonList(new ParameterResolver() {
                    @Override
                    public boolean supportsParameter(ParameterContext parameterContext,
                                                     ExtensionContext extensionContext) {
                        return parameterContext.getParameter()
                            .getType().equals(String.class);
                    }
                    
                    @Override
                    public Object resolveParameter(ParameterContext parameterContext,
                                                   ExtensionContext extensionContext) {
                        return parameter;
                    }
                });
            }
        };
    }
}
```



extension 里的 store：

> Usually, an extension is instantiated only once. So the question becomes relevant: How do you keep the state from one invocation of an extension to the next? The ExtensionContext API provides a Store exactly for this purpose



`org.junit.platform.commons.support` 有一些工具类推荐扩展作者使用

> ... for working with annotations, classes, reflection, and classpath scanning tasks.

`AnnotationSupport`

`ClassSupport`

`ModifierSupport`



> the JUnit Team recommends that developers declare at most one of each type of *lifecycle method* per test class or test interface unless there are no dependencies between such lifecycle methods.



同一个类里的 lifecycle methods 和 test 方法的执行顺序是固定的，但他不告诉你是什么顺序，只是说有序（保证多次构建时方法执行顺序一致）。



