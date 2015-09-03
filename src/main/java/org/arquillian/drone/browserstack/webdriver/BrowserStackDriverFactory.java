/*
 * JBoss, Home of Professional Open Source
 * Copyright 2015, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.arquillian.drone.browserstack.webdriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

import org.jboss.arquillian.config.descriptor.api.ArquillianDescriptor;
import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.spi.Configurator;
import org.jboss.arquillian.drone.spi.Destructor;
import org.jboss.arquillian.drone.spi.DronePoint;
import org.jboss.arquillian.drone.spi.Instantiator;
import org.jboss.arquillian.drone.webdriver.configuration.WebDriverConfiguration;
import org.jboss.arquillian.drone.webdriver.spi.BrowserCapabilities;
import org.jboss.arquillian.drone.webdriver.spi.BrowserCapabilitiesRegistry;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class BrowserStackDriverFactory implements
    Configurator<BrowserStackDriver, WebDriverConfiguration>,
    Instantiator<BrowserStackDriver, WebDriverConfiguration>,
    Destructor<BrowserStackDriver> {

    private static final Logger log = Logger.getLogger(BrowserStackDriverFactory.class.getName());

    @Inject
    protected Instance<BrowserCapabilitiesRegistry> registryInstance;

    public WebDriverConfiguration createConfiguration(ArquillianDescriptor arquillianDescriptor,
        DronePoint<BrowserStackDriver> dronePoint) {

        BrowserCapabilitiesRegistry registry = registryInstance.get();

        // first, try to create a BrowserCapabilities object based on Field/Parameter type of @Drone annotated field
        BrowserCapabilities browser = registry.getEntryFor(BrowserStackDriver.READABLE_NAME);

        WebDriverConfiguration configuration = new WebDriverConfiguration(browser).configure(arquillianDescriptor,
            dronePoint.getQualifier());

        return configuration;
    }

    public void destroyInstance(BrowserStackDriver browserStackDriver) {
        browserStackDriver.quit();
    }

    public BrowserStackDriver createInstance(WebDriverConfiguration configuration) {
        try {

            String url = (String) configuration.getCapabilities().getCapability("url");
            if (isEmpty(url)) {
                String username = (String) configuration.getCapabilities().getCapability("username");
                String automateKey = (String) configuration.getCapabilities().getCapability("automate.key");
                if (isEmpty(username) || isEmpty(automateKey)) {
                    log.severe(
                        "You have to specify either url or username and automate.key in your arquillian descriptor");
                    return null;
                } else {
                    url = "http://" + username + ":" + automateKey + "@hub.browserstack.com/wd/hub";
                }
            }

            return new BrowserStackDriver(new URL(url), configuration.getCapabilities());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean isEmpty(String object) {
        return object == null || object.isEmpty();
    }

    public int getPrecedence() {
        return 0;
    }
}
