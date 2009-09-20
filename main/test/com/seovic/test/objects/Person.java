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


import java.util.Date;


/**
 * Simple Person class that can be used within tests.
 *
 * @author ic  2009.06.16
 */
public class Person
    {
    private long    id;
    private String  name;
    private Date    dob;
    private Address address;

    public Person()
        {
        }

    public Person(Long id, String name, Date dob, Address address)
        {
        this.name = name;
        this.id = id;
        this.dob  = dob;
        this.address = address;
        }

    public String getName()
        {
        return name;
        }

    public void setName(String name)
        {
        this.name = name;
        }

    public long getId()
        {
        return id;
        }

    public void setId(long id)
        {
        this.id = id;
        }

    public Date getDob()
        {
        return dob;
        }

    public void setDob(Date dob)
        {
        this.dob = dob;
        }

    public Address getAddress()
        {
        return address;
        }

    public void setAddress(Address address)
        {
        this.address = address;
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

        Person person = (Person) o;

        return id == person.id
               && !(address != null
                 ? !address.equals(person.address)
                 : person.address != null)
               && !(dob != null
                 ? !dob.equals(person.dob)
                 : person.dob != null)
               && name.equals(person.name);
        }

    @Override
    public int hashCode()
        {
        return (int) (id ^ (id >>> 32));
        }

    @Override
    public String toString()
        {
        return "Person{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", dob=" + dob +
               ", address=" + address +
               '}';
        }
    }
