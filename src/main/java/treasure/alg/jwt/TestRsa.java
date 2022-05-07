package treasure.alg.jwt;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class TestRsa {
    
    public static void main(String[] args) throws Exception {
        genKey();
    }
    
    /**
     * 1. 先得生成公钥私钥，生成一次就行了
     * 2. 根据二进制流，获取公钥或私钥
     *    公钥的类是 x509 那个，私钥的是 pkcs 那个，咱也不知道啥意思他妈的
     * 3. 获取一个 cipher，初始化它为加密模式或解密模式，然后把你那个钥匙给它
     * 4. 然后调用 doFinal 方法，加密或解密二进制流
     * 5. 使用 doFinal 生成的二进制
     * */
    static void genKey() throws
        NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
        InvalidKeySpecException, IllegalBlockSizeException, BadPaddingException {
        
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        SecureRandom secureRandom = new SecureRandom("random-1".getBytes(StandardCharsets.UTF_8));
        keyPairGenerator.initialize(1024, secureRandom);
        KeyPair keyPair = keyPairGenerator.genKeyPair();
        
        byte[] pubKeyBytes = keyPair.getPublic().getEncoded();
        byte[] privateKeyBytes = keyPair.getPrivate().getEncoded();
        // 假设此时二进制写到文件里了
    
        // 假设下面从文件里读取了公私钥的二进制表示
        
        // 这个使用刚才 key 的二进制获取 key，用来加密
        // 这是公钥的类
        X509EncodedKeySpec pub = new X509EncodedKeySpec(pubKeyBytes);
        PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(pub);
    
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        
        String content = "nihao!";
        byte[] encryptedPub = cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
    
        // 私钥的类
        PKCS8EncodedKeySpec pri = new PKCS8EncodedKeySpec(privateKeyBytes);
        PrivateKey privateKey = KeyFactory.getInstance("RSA").generatePrivate(pri);
        
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decrypted = cipher.doFinal(encryptedPub);
        
        // 现在演示的是公钥加密，私钥解密；反过来也一样，就不多说了
        System.out.println(new String(decrypted));
    }
}
