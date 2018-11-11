package com.omnitutor.application.components;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import com.omnitutor.application.domain.LectureDomain;
import com.omnitutor.application.util.CssUtil;
import com.omnitutor.application.util.ErrorMessageUtil;
import com.omnitutor.application.util.FileUploadUtil;
import com.omnitutor.application.util.ResourceUtil;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Video;
import com.vaadin.ui.themes.ValoTheme;

public class CreateLectureDialog extends Dialog {

	private static final long serialVersionUID = 1L;

	private FileUploadUtil fileUpload;

	private Image image;

	private Video video;

	public CreateLectureDialog(FileUploadUtil fileUpload, List<LectureDomain> lectures) {
		super(" Create lecture dialog", VaadinIcons.PLUS_CIRCLE_O, "");
		setStyle();
		initProps(fileUpload);
		initComponents();
	}

	private void initProps(FileUploadUtil fileUpload) {
		this.fileUpload = fileUpload;
	}

	private void setStyle() {
		setIcon(VaadinIcons.PLUS_CIRCLE_O);
		setResizable(false);
	}

	private void initComponents() {
		holder.setWidth("700px");
		holder.setHeight("550px");
		VerticalLayout layout = new VerticalLayout();
		layout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		Component titleAndDescription = createTitleAndDescriptionInputs();
		Component lectureVideoAndImageUpload = createLectureVideoAndImageUpload();
		Component submitLecture = createSubmitButton();
		layout.addComponents(titleAndDescription, lectureVideoAndImageUpload, submitLecture);
		holder.setContent(layout);
	}

	private Component createTitleAndDescriptionInputs() {
		Panel holder = new Panel("Title, description and duration in minutes");
		holder.setWidth("100%");
		holder.setHeight("300px");
		VerticalLayout layout = new VerticalLayout();
		layout.setSizeFull();
		TextField titleInput = new TextField();
		titleInput.setWidth("100%");
		titleInput.setPlaceholder("Enter the title of a lecture");
		TextArea descInput = new TextArea();
		descInput.setSizeFull();
		descInput.setPlaceholder("Enter description");
		TextField durationInput = new TextField();
		durationInput.setWidth("100%");
		durationInput.setPlaceholder("Enter the duration of a lecture");
		layout.addComponents(titleInput, descInput, durationInput);
		layout.setExpandRatio(titleInput, 1);
		layout.setExpandRatio(descInput, 2);
		layout.setExpandRatio(durationInput, 1);
		holder.setContent(layout);
		return holder;
	}

	private Component createLectureVideoAndImageUpload() {
		Panel holder = new Panel("Lecture thumbnail and video");
		holder.setWidth("100%");
		holder.setHeight("375px");
		HorizontalLayout layout = new HorizontalLayout();
		layout.setSizeFull();
		Component lectureImage = createLectureImage();
		Component lectureVideo = createLectureVideo();
		layout.addComponents(lectureImage, lectureVideo);
		holder.setContent(layout);
		return holder;
	}

	private Component createLectureImage() {
		VerticalLayout layout = new VerticalLayout();
		image = new Image(null, new ThemeResource(ResourceUtil.IMAGE_PLACEHOLDER));
		image.setSizeFull();
		image.addStyleName(CssUtil.IMAGE_FIT);
		Upload uploadImage = new Upload(null, fileUpload);
		uploadImage.setButtonCaption("Upload image");
		uploadImage.setSizeFull();
		uploadImage.addSucceededListener(e -> setImage(uploadImage));
		layout.addComponents(image, uploadImage);
		layout.setExpandRatio(image, 4);
		layout.setExpandRatio(uploadImage, 1);
		layout.setSizeFull();
		return layout;
	}

	private Object setImage(Upload upload) {
		FileUploadUtil reciever = (FileUploadUtil) upload.getReceiver();
		ByteArrayOutputStream data = reciever.getData();
		if (!reciever.getFileType().equals(FileUploadUtil.IMAGE)) {
			ErrorMessageUtil.showErrorDialog("Uploaded file is not an image.");
			return this;
		}
		StreamSource source = new StreamSource() {
			private static final long serialVersionUID = 1L;

			public InputStream getStream() {
				return new ByteArrayInputStream(data.toByteArray());
			}
		};
		image.setSource(new StreamResource(source, new Date().toString()));
		return this;
	}

	private Component createLectureVideo() {
		VerticalLayout layout = new VerticalLayout();
		layout.addStyleName(CssUtil.SOLID_BORDER_LEFT);
		video = new Video();
		video.setSizeFull();
		video.addStyleName(CssUtil.IMAGE_FIT);
		Upload uploadVideo = new Upload(null, fileUpload);
		uploadVideo.setButtonCaption("Upload video");
		uploadVideo.setSizeFull();
		uploadVideo.addSucceededListener(e -> setVideo(uploadVideo));
		layout.addComponents(video, uploadVideo);
		layout.setExpandRatio(video, 4);
		layout.setExpandRatio(uploadVideo, 1);
		layout.setSizeFull();
		return layout;
	}
	
	private Object setVideo(Upload upload) {
		FileUploadUtil reciever = (FileUploadUtil) upload.getReceiver();
		ByteArrayOutputStream data = reciever.getData();
		if (!reciever.getFileType().equals(FileUploadUtil.VIDEO)) {
			ErrorMessageUtil.showErrorDialog("Uploaded file is not an video.");
			return this;
		}
		StreamSource source = new StreamSource() {
			private static final long serialVersionUID = 1L;

			public InputStream getStream() {
				return new ByteArrayInputStream(data.toByteArray());
			}
		};
		StreamResource resource = new StreamResource(source, new Date().toString());
		resource.setMIMEType(reciever.getMimeType());
		resource.setBufferSize(data.size());
		resource.setCacheTime(-1);
		video.setSource(resource);
		return this;
	}

	private Component createSubmitButton() {
		Button submit = new Button("Add lecture");
		submit.setIcon(VaadinIcons.DATABASE);
		submit.addStyleName(ValoTheme.BUTTON_PRIMARY);
		return submit;
	}

}
