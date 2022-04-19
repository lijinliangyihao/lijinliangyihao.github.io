

#### `spring.main.web-application-type`

```java
@SpringBootTest(properties = "spring.main.web-application-type=reactive")
class MyWebFluxTests {

}
```

springboot 会自动帮你找 `@SpringBootApplication` or `@SpringBootConfiguration`，不必像 springmvc-test 那样指定 @ContextConfiguration



@ComponentScan 会让 @springbootapplication 上的 excludefilters 失效

@TestConfiguration 干啥的？说的不是很明白



#### 注入启动参数

```java
@SpringBootTest(args = "--app.test=one")
class MyApplicationArgumentTests {

    // ApplicationArguments 新鲜！
    @Test
    void applicationArgumentsPopulated(@Autowired ApplicationArguments 新鲜！ args) {
        assertThat(args.getOptionNames()).containsOnly("app.test");
        assertThat(args.getOptionValues("app.test")).containsOnly("one");
    }

}
```



#### @SpringBootTest

`webEnvironment` 默认是 mock，不启动 server:

```java
@SpringBootTest
@AutoConfigureMockMvc
class MyMockMvcTests {

    @Test
    void testWithMockMvc(@Autowired MockMvc mvc) throws Exception {
        mvc.perform(get("/"))
           .andExpect(status().isOk())
           .andExpect(content().string("Hello World"));
    }

    // If Spring WebFlux is on the classpath,
    // you can drive MVC tests with a WebTestClient
    @Test
    void testWithWebTestClient(@Autowired WebTestClient webClient) {
        webClient.get().uri("/")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo("Hello World");
    }

}

```



#### 不想启动 applicationContext，只想有个 web 层

`@WebMvcTest`

```java
@SpringBootTest
@AutoConfigureWebTestClient
class MyMockWebTestClientTests {

    @Test
    void exampleTest(@Autowired WebTestClient webClient) {
        webClient
            .get().uri("/")
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class).isEqualTo("Hello World");
    }

}
```



#### 真正启动一个 server

比如测错误页面，需要真实启动一个server：

```java
// 推荐随机端口但没说理由
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
// 据说可以注入一个 port
@LocalServerPort
class MyRandomPortWebTestClientTests {

    @Test
    void exampleTest(@Autowired WebTestClient webClient) {
        webClient
            .get().uri("/")
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class).isEqualTo("Hello World");
    }

}
```

上面那个需要有 webflux 组件，如果没有，则用下面这招：

```java
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class MyRandomPortTestRestTemplateTests {

    @Test
    void exampleTest(@Autowired TestRestTemplate restTemplate) {
        String body = restTemplate.getForObject("/", String.class);
        assertThat(body).isEqualTo("Hello World");
    }

}
```

#### 定制化 webtestclient

`WebTestClientBuilderCustomizer` 搞一个bean



#### jmx 默认禁止

因为 context 是被缓存的，可能导致多份 jmx，可以使用下面这招测试 jmx：

```java
@ExtendWith(SpringExtension.class)
@SpringBootTest(properties = "spring.jmx.enabled=true")
@DirtiesContext
class MyJmxTests {

    @Autowired
    private MBeanServer mBeanServer;

    @Test
    void exampleTest() throws MalformedObjectNameException {
        assertThat(this.mBeanServer.getDomains()).contains("java.lang");
        // ...
    }

}
```



#### 指标默认不注册

> Regardless of your classpath, meter registries, except the in-memory backed, are not auto-configured when using @SpringBootTest.
>
> If you need to export metrics to a different backend as part of an integration test, annotate it with @AutoConfigureMetrics.



#### mockbean

`@MockBean `自动帮你注入 mockito mock 的bean，前提是你使用 `@SpringBootTest`，否则：

```java
// 不使用 springboottest 注解你得这样做
@ContextConfiguration(classes = MyConfig.class)
@TestExecutionListeners({ MockitoTestExecutionListener.class, ResetMocksTestExecutionListener.class })
class MyTests {

}
```

看看效果，贼鸡巴丝滑：

```java
@SpringBootTest
class MyTests {

    @Autowired
    private Reverser reverser;

    @MockBean
    private RemoteService remoteService;

    @Test
    void exampleTest() {
        given(this.remoteService.getValue()).willReturn("spring");
        String reverse = this.reverser.getReverseValue(); // Calls injected RemoteService
        assertThat(reverse).isEqualTo("gnirps");
    }

}

```

使用 `@SpyBean` 处理已存在的 bean。

warning：

> @MockBean cannot be used to mock the behavior of a bean that is exercised during application context refresh.  By the time the test is executed, the application context refresh has completed and it is too late to configure the mocked behavior. We recommend using a @Bean method to create and configure the mock in this situation.



`@MockBean` `@SpyBean`可能会增加 context 数量；

`@SpyBean` 配合 `@Cacheable`，使用参数名作为 key 时，得用 `-parameters` 参数编译 class

使用 @SpyBean 判断方法行为时，如果类被代理了，需要用下面这招得到真实对象 :

​	`AopTestUtils.getTargetObject(yourProxiedSpy)`



#### mock 增强后的 final 方法

增加 `org.mockito:mockito-inline` 依赖，使用相关技术？？



#### 只测试一部分

> ... `@…Test` annotation that loads the ApplicationContext and one or more `@AutoConfigure…` annotations that can be used to customize auto-configuration settings

它们只加载一小部分。

> `@…Test` annotations provide an excludeAutoConfiguration attribute
>
> Alternatively, you can use `@ImportAutoConfiguration#exclude`

`@…Test` 每个类只能有一个，如果想多个，需要搭配 `@AutoConfigure…`。



#### 只导入必要部分

`@TestConfiguration` 放到测试类上，导致 `src/test/java` 里的类不被扫描：

```java
@SpringBootTest
@Import(MyTestsConfiguration.class)
class MyTests {

    @Test
    void exampleTest() {
        // ...
    }

}
```





#### 切片示例

切片就是 `@...Test`

##### @JsonTest

一个注解，让你自动拥有了 JacksonTester GsonTester JsonbTester BasicJsonTester（用来测 string）：

```
@JsonTest
class MyJsonTests {

    @Autowired
    private JacksonTester<VehicleDetails> json;

    @Test
    void serialize() throws Exception {
        VehicleDetails details = new VehicleDetails("Honda", "Civic");
        // Assert against a `.json` file in the same package as the test
        assertThat(this.json.write(details)).isEqualToJson("expected.json");
        // Or use JSON path based assertions
        assertThat(this.json.write(details)).hasJsonPathStringValue("@.make");
        assertThat(this.json.write(details)).extractingJsonPathStringValue("@.make").isEqualTo("Honda");
    }

    @Test
    void deserialize() throws Exception {
        String content = "{\"make\":\"Ford\",\"model\":\"Focus\"}";
        assertThat(this.json.parse(content)).isEqualTo(new VehicleDetails("Ford", "Focus"));
        assertThat(this.json.parseObject(content).getMake()).isEqualTo("Ford");
    }

}
```

使用 json库和 assertj 断言 bean：

```java
@Test
void someTest() throws Exception {
    SomeObject value = new SomeObject(0.152f);
    assertThat(this.json.write(value)).extractingJsonPathNumberValue("@.test.numberValue")
            .satisfies((number) -> assertThat(number.floatValue()).isCloseTo(0.15f, within(0.01f)));
}
```

##### `@WebMvcTest`

> `@WebMvcTest` auto-configures the Spring MVC infrastructure and limits scanned beans to `@Controller`, `@ControllerAdvice`, `@JsonComponent`, `Converter`, `GenericConverter`, `Filter`, `HandlerInterceptor`, `WebMvcConfigurer`, `WebMvcRegistrations`, and `HandlerMethodArgumentResolver`. Regular `@Component` and `@ConfigurationProperties` beans are not scanned when the `@WebMvcTest` annotation is used. `@EnableConfigurationProperties` can be used to include `@ConfigurationProperties` beans

很详细，只扫描 controller 相关的类，这样 service 层就不起作用了。。

连 Jackson 都没了，还得自己 `@Import` 配置类

他说配合 `@MockBean ` 测试单个 controller。



只用它一个注解就够了：

```java
@WebMvcTest(UserVehicleController.class)
class MyControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserVehicleService userVehicleService;

    @Test
    void testExample() throws Exception {
        given(this.userVehicleService.getVehicleDetails("sboot"))
            .willReturn(new VehicleDetails("Honda", "Civic"));
        this.mvc.perform(get("/sboot/vehicle").accept(MediaType.TEXT_PLAIN))
            .andExpect(status().isOk())
            .andExpect(content().string("Honda Civic"));
    }

}
```

`@AutoConfigureMockMvc` 可以进行额外的配置。

```java
@WebMvcTest(UserVehicleController.class)
class MyHtmlUnitTests {

    @Autowired
    private WebClient webClient;

    @MockBean
    private UserVehicleService userVehicleService;

    @Test
    void testExample() throws Exception {
        given(this.userVehicleService.getVehicleDetails("sboot")).willReturn(new VehicleDetails("Honda", "Civic"));
        HtmlPage page = this.webClient.getPage("/sboot/vehicle.html");
        assertThat(page.getBody().getTextContent()).isEqualTo("Honda Civic");
    }

}
```

如果类路径有 security 相关的类，`WebSecurityConfigurer` 也被扫描。

`WebDriver` 这个东西默认不是单例的，可以 `@Scope("singleton")` 修改。

##### `@WebFluxTest`

扫描：`@Controller`,  `@ControllerAdvice`,  `@JsonComponent`,  `Converter`,  `GenericConverter`,  `WebFilter`,  `WebFluxConfigurer`



```java
@WebFluxTest(UserVehicleController.class)
class MyControllerTests {

    @Autowired
    private WebTestClient webClient;

    @MockBean
    private UserVehicleService userVehicleService;

    @Test
    void testExample() throws Exception {
        given(this.userVehicleService.getVehicleDetails("sboot"))
            .willReturn(new VehicleDetails("Honda", "Civic"));
        this.webClient.get().uri("/sboot/vehicle").accept(MediaType.TEXT_PLAIN).exchange()
            .expectStatus().isOk()
            .expectBody(String.class).isEqualTo("Honda Civic");
    }

}
```

默认不注册 `RouterFunction`，想测，自己 `@Import` 或使用 `@SprintBootTest`



##### `@JdbcTest`

只需一个 `DataSource`

关闭测试的事务管理：

```java
@JdbcTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class MyTransactionalTests {

}
```



##### `@DataRedisTest`

类路径有 `@RedisHash` 自动注册。

```java
@DataRedisTest
class MyDataRedisTests {

    @Autowired
    private SomeRepository repository;

    // ...

}

```



##### `@RestClientTest`

自动装配下面这些东西：

```
Jackson, GSON, Jsonb, RestTemplateBuilder, MockRestServiceServer
```



```java
@RestClientTest(RemoteVehicleDetailsService.class)
class MyRestClientTests {

    @Autowired
    private RemoteVehicleDetailsService service;

    @Autowired
    private MockRestServiceServer server;

    @Test
    void getVehicleDetailsWhenResultIsSuccessShouldReturnDetails() throws Exception {
        this.server.expect(requestTo("/greet/details")).andRespond(withSuccess("hello", MediaType.TEXT_PLAIN));
        String greeting = this.service.callRestService();
        assertThat(greeting).isEqualTo("hello");
    }

}
```



#### 引入其它的 slice

```java
@JdbcTest
@ImportAutoConfiguration(IntegrationAutoConfiguration.class)
class MyJdbcTests {

}
```

不能用 `@Import`，因为这是 `AutoConfiguration`

> Make sure to not use the regular  annotation to import auto-configurations as they are handled in a specific way by Spring Boot



也可以自动注册 autoconfig：

```
# META-INF/spring.factories
org.springframework.boot.test.autoconfigure.jdbc.JdbcTest=com.example.IntegrationAutoConfiguration
```



*定制化 slice：*

A slice or `@AutoConfigure…` annotation can be customized this way as long as it is meta-annotated with `@ImportAutoConfiguration`



举个反例：

```java
// 所有测试都启动 springbatch
@SpringBootApplication
@EnableBatchProcessing
public class MyApplication {

}

// 应该放到另一个配置类上
@Configuration(proxyBeanMethods = false)
@EnableBatchProcessing
public class MyBatchConfiguration {

}
```

***懂了！***

> Depending on the complexity of your application, you may either have a single @Configuration class for your customizations or one class per domain area. **The latter approach lets you enable it in one of your tests, if necessary, with the @Import annotation**



***Test slices exclude `@Configuration` classes from scanning.*** 

```java
// 默认不扫
@Configuration(proxyBeanMethods = false)
public class MyWebConfiguration {

    @Bean
    public WebMvcConfigurer testConfigurer() {
        return new WebMvcConfigurer() {
            // ...
        };
    }

}
```

```java
// 扫
@Component
public class MyWebMvcConfigurer implements WebMvcConfigurer {

    // ...

}
```

再举个反例：

```java
@SpringBootApplication
@ComponentScan({ "com.example.app", "com.example.another" })
public class MyApplication {

    // ...

}
```

这导致 slice 失效，slice 本来只扫描相关的类结果遇到你这个 `@ComponentScan` 它把不想要的类也扫进来了，所以：

> moving the custom directive to a separate class is a good way to fix this issue



#### TEST UTILITIES

`ConfigDataApplicationContextInitializer` 用这个类只能帮你加载 application.properties，`@Value("${…}")` 就失效了，你还得配置 `PropertySourcesPlaceholderConfigurer` 帮着修改属性值

```java
@ContextConfiguration(classes = Config.class, initializers = ConfigDataApplicationContextInitializer.class)
class MyConfigFileTests {

    // ...

}
```

#####  `TestPropertyValues`

这个类可以往 env 中加属性：

```java
class MyEnvironmentTests {

    @Test
    void testPropertySources() {
        MockEnvironment environment = new MockEnvironment();
        TestPropertyValues.of("org=Spring", "name=Boot").applyTo(environment);
        assertThat(environment.getProperty("name")).isEqualTo("Boot");
    }

}
```

##### `OutputCaptureExtension` 捕获输出

```java
@ExtendWith(OutputCaptureExtension.class)
class MyOutputCaptureTests {

    @Test
    void testName(CapturedOutput output) {
        System.out.println("Hello World!");
        assertThat(output).contains("World");
    }

}
```



##### `TestRestTemplate`

可以 new 也可以 autowire（得有 `@SpringBootTest`），可以通过配置类配置：

```java
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class MySpringBootTests {

    @Autowired
    private TestRestTemplate template;

    @Test
    void testRequest() {
        HttpHeaders headers = this.template.getForEntity("/example", String.class).getHeaders();
        assertThat(headers.getLocation()).hasHost("other.example.com");
    }

    @TestConfiguration(proxyBeanMethods = false)
    static class RestTemplateBuilderConfiguration {

        @Bean
        RestTemplateBuilder restTemplateBuilder() {
            return new RestTemplateBuilder().setConnectTimeout(Duration.ofSeconds(1))
                    .setReadTimeout(Duration.ofSeconds(1));
        }

    }

}
```

url 中无 host port，则访问 `the embedded server`