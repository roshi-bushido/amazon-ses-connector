package org.mule.modules.amazonses;

import java.util.List;

import org.apache.log4j.Logger;
import org.mule.api.annotations.Config;
import org.mule.api.annotations.Connector;
import org.mule.api.annotations.Processor;
import org.mule.modules.amazonses.config.ConnectorConfig;

import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;

@Connector(name="amazon-ses", friendlyName="Amazon SES")
public class AmazonSESConnector {
	private Logger logger = Logger.getLogger(AmazonSESConnector.class);
	
    @Config
    ConnectorConfig config;

    @Processor
    public void sendEmail(List<String> destinationAddresses, String from, String subject, String body, Boolean asHTML) {
    	String[] addressList = new String[destinationAddresses.size()];
    	for (int i=0; i < destinationAddresses.size(); i++) {
			addressList[i] = destinationAddresses.get(i);
		}

    	Destination destination = new Destination().withToAddresses(addressList);
        Content emailSubject = new Content().withData(subject);
        Content textBody = new Content().withData(body); 
        Body emailBody = new Body().withText(textBody);
        Message message = new Message().withSubject(emailSubject).withBody(emailBody);
        SendEmailRequest request = new SendEmailRequest().withSource(from).withDestination(destination).withMessage(message);
        this.getConfig().getClient().sendEmail(request);  
    }

    public ConnectorConfig getConfig() {
        return config;
    }

    public void setConfig(ConnectorConfig config) {
        this.config = config;
    }

}