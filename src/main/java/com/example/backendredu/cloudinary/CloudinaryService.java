package com.example.backendredu.cloudinary;

import com.cloudinary.Cloudinary;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {
	
	private final Cloudinary cloudinary;
	
	public CloudinaryService() {
		Dotenv dotenv = Dotenv.configure().load();
		String cloudinaryUrl = dotenv.get("CLOUDINARY_URL");
		this.cloudinary = new Cloudinary(cloudinaryUrl);
	}
	
	public String uploadImage(MultipartFile file, String folder, String publicId) throws IOException {
		Map uploadResult = cloudinary.uploader().upload(file.getBytes(), Map.of(
				"folder", folder,
				"public_id", publicId,
				"overwrite", true,
				"resource_type", "image"
		));
		return uploadResult.get("secure_url").toString();
	}
}
