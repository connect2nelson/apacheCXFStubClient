package com.example.demo;


import com.abm.weatherxnet.GlobalWeather;
import com.abm.weatherxnet.GlobalWeatherSoap;
import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.headers.Header;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxb.JAXBDataBinding;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.ws.addressing.AddressingProperties;
import org.apache.cxf.ws.addressing.AttributedURIType;
import org.apache.cxf.ws.addressing.EndpointReferenceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.soap.SOAPBinding;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class WeatherDataController {

    @Autowired
    private MutualSslConfig mutualSslConfig;

    @Autowired
    private ProxyConfig proxyConfig;

    @GetMapping("/api/weather/{city}/{country}")
    public ResponseEntity getWeatherByCityAndCountry(@PathVariable("country") String countryName,
                                                     @PathVariable("city") String cityName) {
        try {
            GlobalWeatherSoap portWithWSDL = createPortWithWSDL();

            AddressingProperties addressingProperties = new AddressingProperties();
            EndpointReferenceType endpointReferenceType = new EndpointReferenceType();
            AttributedURIType attributedURIType = new AttributedURIType();
            attributedURIType.setValue("http://www.webservicex.com/globalweather.asmx");
            endpointReferenceType.setAddress(attributedURIType);
            addressingProperties.setTo(attributedURIType);


            Client client = ClientProxy.getClient(portWithWSDL);
            client.getRequestContext().put("javax.xml.ws.addressing.context", addressingProperties);


            //For logging soap xml request and responses
            client.getInInterceptors().add(new LoggingInInterceptor());
            client.getOutInterceptors().add(new LoggingOutInterceptor());

            HTTPConduit http = (HTTPConduit) client.getConduit();
            // commenting out this for now as we dont have an proxy or ssl config to be autowired
            // as the current server http://www.webservicex.com/globalweather.asmx is publicly accessible over http
            // setMutualSslConfig(http);  // config ssl , auth
            // setProxyConfiguration(http); //
            setSoapHeaders(client);

            String weather = portWithWSDL.getWeather(cityName, countryName);

            return new ResponseEntity<>(weather, HttpStatus.OK);

        } catch (Exception fault) {
            System.out.println("Something went wrong : " + fault);
        }
        return null;
    }

    private AddressingProperties createAddressingProperties(String setEndpointAttribute) {
        AddressingProperties addressingProperties = new AddressingProperties();
        EndpointReferenceType endpointReferenceType = new EndpointReferenceType();
        AttributedURIType attributedURIType = new AttributedURIType();

        attributedURIType.setValue(setEndpointAttribute);
        endpointReferenceType.setAddress(attributedURIType);
        addressingProperties.setTo(attributedURIType);
        return addressingProperties;
    }

    private GlobalWeatherSoap createPortWithWSDL() throws MalformedURLException {
        GlobalWeather globalWeather = new GlobalWeather();
        return globalWeather.getGlobalWeatherSoap();
    }

    private GlobalWeatherSoap createPortWithoutWSDL() {

        QName globalWeatherServiceQName = new QName("http://www.webserviceX.NET", "GlobalWeather");
        Service weatherService = Service.create(globalWeatherServiceQName);
        QName globalWeatherSoapPortQName = new QName("http://www.webserviceX.NET",
                "GlobalWeatherSoap");
        weatherService.addPort(globalWeatherSoapPortQName, SOAPBinding.SOAP11HTTP_BINDING,
                "https://weather.data.com/services"); //dummy http site specification
        return weatherService
                .getPort(globalWeatherSoapPortQName, GlobalWeatherSoap.class);
    }

    private void setMutualSslConfig(HTTPConduit http) {
        TLSClientParameters parameters = new TLSClientParameters();
        parameters.setSSLSocketFactory(mutualSslConfig.sslContext.getSocketFactory());
        http.setTlsClientParameters(parameters);
    }

    private void setProxyConfiguration(HTTPConduit http) {
        http.getClient().setProxyServer(proxyConfig.getHostname());
        http.getClient().setProxyServerPort(proxyConfig.getPort());
        http.getAuthorization().setUserName(proxyConfig.getUser());
        http.getAuthorization().setPassword(proxyConfig.getPassword());
    }

    private void setSoapHeaders(Client client) throws JAXBException {
        List<Header> headersList = new ArrayList<>();


        Header dummySoapHeader = new Header(new QName("http://www.webserviceX.NET", "System"),
                "TestingSoapHeader", new JAXBDataBinding(String.class));
        headersList.add(dummySoapHeader);

        client.getRequestContext().put(Header.HEADER_LIST, headersList);
    }


}
