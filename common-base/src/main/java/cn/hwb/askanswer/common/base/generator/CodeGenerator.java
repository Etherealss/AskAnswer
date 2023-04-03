package cn.hwb.askanswer.common.base.generator;

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.util.Scanner;

/**
 * 执行 main 方法控制台输入模块表名回车自动生成对应项目目录中
 * https://blog.csdn.net/BADAO_LIUMANG_QIZHI/article/details/89518466
 * @author wtk
 * @date 2021-10-03
 */
public class CodeGenerator {
    private static final String DB_NAME = "nowander";
    private static final String OUT_PATH = "D:\\MyProgrammingWorld\\IdeaWorkSpace\\nowander\\docs";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/ask_answer";
    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String[] TABLE_NAMES = new String[]{
            "like_record_test1", "like_record_test2"
    };
    private static final String DB_USER = "root";
    public static final String PASSWORD = "123456";


    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入" + tip + "：");
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotBlank(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }

    public static void main(String[] args) {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // ----------------------------全局配置----------------------------
        GlobalConfig gc = new GlobalConfig();
        // 项目路径
        // 生成路径
        gc.setOutputDir(OUT_PATH);
        System.out.println("生成路径：" + gc.getOutputDir());
        // 作者
        gc.setAuthor("wtk");
        // 是否打开所在文件夹
        gc.setOpen(false);
        // 实体属性 Swagger2 注解
        gc.setSwagger2(false);
        // 重复生成时覆盖已有文件
        gc.setFileOverride(false);
        // 会在mapper.xml中生成一个<ResultMap>映射所有字段
        gc.setBaseResultMap(true);
        // 设置实体类命名：%s表示表名，自动驼峰
        gc.setEntityName("%s");
        gc.setMapperName("%sMapper");
        gc.setXmlName("%sMapper");
        gc.setServiceName("%sService");
        gc.setServiceImplName("%sServiceImpl");

        // 将全局配置设置到AutoGenerator中
        mpg.setGlobalConfig(gc);

        // ----------------------------数据源配置----------------------------
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl(DB_URL);
        // dsc.setSchemaName("public");
        dsc.setDriverName(DB_DRIVER);
        dsc.setUsername(DB_USER);
        dsc.setPassword(PASSWORD);
        dsc.setTypeConvert(new MySqlTypeConvert() {
            @Override
            public IColumnType processTypeConvert(GlobalConfig config, String fieldType) {
                System.out.println("转换类型：" + fieldType);
                //tinyint转换成Boolean
//                if (fieldType.toLowerCase().contains("tinyint")) {
//                    return DbColumnType.BOOLEAN;
//                }
                //将数据库中datetime转换成date
                if (fieldType.toLowerCase().contains("datetime")) {
                    return DbColumnType.DATE;
                }
                return super.processTypeConvert(config, fieldType);
            }
        });

        mpg.setDataSource(dsc);

        // ----------------------------包配置----------------------------
        PackageConfig pc = new PackageConfig();
        // 模块名，比如sys
//        pc.setModuleName(scanner("模块名"));
        // 包名
        pc.setParent("com.nowander");
        pc.setEntity("pojo.po");
        pc.setMapper("mapper");
        pc.setService("service");
        pc.setController("controller");
        mpg.setPackageInfo(pc);

        // ----------------------------自定义配置----------------------------
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // 可以不写东西，但是一定要这么写
            }
        };

        // 如果模板引擎是 freemarker
//        String templatePath = "/templates/mapper.xml.ftl";
        // 如果模板引擎是 velocity
        // String templatePath = "/templates/mapper.xml.vm";

        // ----------------------------自定义输出配置----------------------------
        // 可以配置mapper文件生成到哪个路径下
//        List<FileOutConfig> focList = new ArrayList<>();
        // 自定义配置会被优先输出
//        focList.add(new FileOutConfig(templatePath) {
//            @Override
//            public String outputFile(TableInfo tableInfo) {
//                // 自定义输出路径和文件名
//                // 如果Entity设置了前后缀，xml的名称会跟着发生变化
//                return projectPath + "/src/main/resources/mapper/"
////                        + pc.getModuleName() + "/"
//                        + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
//            }
//        });
        /*
        cfg.setFileCreate(new IFileCreate() {
            @Override
            public boolean isCreate(ConfigBuilder configBuilder, FileType fileType, String filePath) {
                // 判断自定义文件夹是否需要创建
                checkDir("调用默认方法创建的目录，自定义目录用");
                if (fileType == FileType.MAPPER) {
                    // 已经生成 mapper 文件判断存在，不想重新生成返回 false
                    return !new File(filePath).exists();
                }
                // 允许生成模板文件
                return true;
            }
        });
        */
//        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);

        // ----------------------------配置模板----------------------------
//        TemplateConfig templateConfig = new TemplateConfig();

        // 配置自定义输出模板
        // 指定自定义模板路径，注意不要带上.ftl/.vm, 会根据使用的模板引擎自动识别
//         templateConfig.setEntity("templates/entity2.java");
        // templateConfig.setService();
        // templateConfig.setController();

        // 不生成默认的Mapper.xml，如果你在上面的自定义输出配置中，
        // 配置了mapper文件的生成路径，那么这里需要设置为null，
        // 否则它还是会自动生成到你的/src/main/java/目录下
//        templateConfig.setXml(null);
//        mpg.setTemplate(templateConfig);

        // ----------------------------策略配置----------------------------
        StrategyConfig strategy = new StrategyConfig();
        // 表名生成策略：下划线转驼峰
        strategy.setNaming(NamingStrategy.underline_to_camel);
        // 列名生成策略
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
//        strategy.setSuperEntityClass("你自己的父类实体,没有就不用设置!");
        // lombok
        strategy.setEntityLombokModel(true);
        // Controller类上的注解是否使用@RestController
        strategy.setRestControllerStyle(true);
        // Controller公共父类
//        strategy.setSuperControllerClass("你自己的父类控制器,没有就不用设置!");
        // 写于父类中的公共字段
//        strategy.setSuperEntityColumns("id");

        // 指定要进行代码生成的数据库表
        strategy.setInclude(TABLE_NAMES);
        // 按模糊匹配来指定表：
        // strategy.setLikeTable(new LikeTable("sys_"))
        // 设置驼峰转连接符，比如
        // false时：pms_product --> @RequestMapping(".pms/pmsProduct")
        // true时 ：pms_product --> @RequestMapping(".pms/pms_product")
        strategy.setControllerMappingHyphenStyle(false);
        // 命名生成类时过滤表名前缀。比如 sys_user -> User
//        strategy.setTablePrefix(pc.getModuleName() + "_");
        mpg.setStrategy(strategy);
        mpg.execute();
    }
}

