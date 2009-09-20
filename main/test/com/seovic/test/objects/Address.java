/*
 * Copyright 2009 Aleksandar Seovic
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.seovic.test.objects;


/**
 * Simple Address class that can be used within tests.
 *
 * @author Aleksandar Seovic  2009.09.20
 */
public class Address
    {
    private String street;
    private String city;
    private String country;

    public Address(String street, String city, String country)
        {
        this.street = street;
        this.city = city;
        this.country = country;
        }

    public String getStreet()
        {
        return street;
        }

    public void setStreet(String street)
        {
        this.street = street;
        }

    public String getCity()
        {
        return city;
        }

    public void setCity(String city)
        {
        this.city = city;
        }

    public String getCountry()
        {
        return country;
        }

    public void setCountry(String country)
        {
        this.country = country;
        }

    @Override
    public boolean equals(Object o)
        {
        if (this == o)
            {
            return true;
            }
        if (o == null || getClass() != o.getClass())
            {
            return false;
            }

        Address address = (Address) o;

        return !(city != null
                 ? !city.equals(address.city)
                 : address.city != null)
               && !(country != null
                 ? !country.equals(address.country)
                 : address.country != null)
               && !(street != null
                 ? !street.equals(address.street)
                 : address.street != null);
        }

    @Override
    public int hashCode()
        {
        int result = street != null ? street.hashCode() : 0;
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (country != null ? country.hashCode() : 0);
        return result;
        }

    @Override
    public String toString()
        {
        return "Address{" +
               "street='" + street + '\'' +
               ", city='" + city + '\'' +
               ", country='" + country + '\'' +
               '}';
        }
    }
