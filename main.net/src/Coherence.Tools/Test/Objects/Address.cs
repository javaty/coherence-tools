using System;
using Tangosol.IO.Pof;

namespace Seovic.Coherence.Test.Objects
{
    [Serializable]
    public class Address : IPortableObject
    {
        private String street;
        private String city;
        private String country;

        public Address()
        {
        }

        public Address(string street, string city, string country)
        {
            this.street = street;
            this.city = city;
            this.country = country;
        }

        public string Street
        {
            get { return street; }
            set { street = value; }
        }

        public string City
        {
            get { return city; }
            set { city = value; }
        }

        public string Country
        {
            get { return country; }
            set { country = value; }
        }

        public void ReadExternal(IPofReader reader)
        {
            street  = reader.ReadString(0);
            city    = reader.ReadString(1);
            country = reader.ReadString(2);
        }

        public void WriteExternal(IPofWriter writer)
        {
            writer.WriteString(0, street);
            writer.WriteString(1, city);
            writer.WriteString(2, country);
        }

        public bool Equals(Address other)
        {
            if (ReferenceEquals(null, other)) return false;
            if (ReferenceEquals(this, other)) return true;
            return Equals(other.street, street) && Equals(other.city, city) && Equals(other.country, country);
        }

        public override bool Equals(object obj)
        {
            if (ReferenceEquals(null, obj)) return false;
            if (ReferenceEquals(this, obj)) return true;
            if (obj.GetType() != typeof (Address)) return false;
            return Equals((Address) obj);
        }

        public override int GetHashCode()
        {
            unchecked
            {
                int result = street.GetHashCode();
                result = (result*397) ^ city.GetHashCode();
                result = (result*397) ^ country.GetHashCode();
                return result;
            }
        }
    }
}
