package cateye.utils;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.teaopenapi.models.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AliSMSClient {
    @Value("${ali-sms.access-key-id:}")
    private String accessKeyId;
    @Value("${ali-sms.access-key-secret:}")
    private String accessKeySecret;
    @Value("${ali-sms.endpoint:}")
    private String endpoint;

    /**
     * 使用AK&SK初始化账号Client
     * @return Client
     * @throws Exception
     */
    @Bean
    public Client createClient() throws Exception {
        Config config = new Config()
                // 您的 AccessKey ID
                .setAccessKeyId(accessKeyId)
                // 您的 AccessKey Secret
                .setAccessKeySecret(accessKeySecret);
        // 访问的域名
        config.endpoint = endpoint;
        return new Client(config);
    }
}