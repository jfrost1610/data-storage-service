/**
 * 
 */
package com.frost.documentservice.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author jobin
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { CryptoService.class })
@TestPropertySource(properties = { "aes.secret=test-secret-junit" })
public class CryptoServiceTest {

	@Autowired
	private CryptoService service;

	@Test
	public void testEncryptionDecryption() {

		String originalData = "test-data-validate-encryption";

		String encryptedData = service.encrypt(originalData);

		Assert.assertNotEquals(originalData, encryptedData);

		String decryptedData = service.decrypt(encryptedData);

		Assert.assertEquals(originalData, decryptedData);

	}

}
