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

import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class ConfigureBrowserStackImpl implements ConfigureBrowserStackCapabilities {
    public void configure(DesiredCapabilities capabilities) {
        capabilities.setCapability("browserName", "firefox");
        capabilities.setCapability("browser_version", "38");
        capabilities.setCapability("os", "Windows");
        capabilities.setCapability("os_version", "8.1");
        capabilities.setCapability("browserstack.debug", "true");
    }
}