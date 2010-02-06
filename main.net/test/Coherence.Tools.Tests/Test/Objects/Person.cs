using System;
using Tangosol.IO.Pof;

namespace Seovic.Test.Objects
{
    [Serializable]
    public class Person : IPortableObject
    {
        private long id;
        private string name;
        private DateTime dob;
        private Address address;

        public Person()
        {
        }

        public Person(long id, string name)
        {
            this.id = id;
            this.name = name;
        }

        public Person(long id, string name, DateTime dob, Address address)
        {
            this.id = id;
            this.name = name;
            this.dob = dob;
            this.address = address;
        }

        public long Id
        {
            get { return id; }
            set { id = value; }
        }

        public string Name
        {
            get { return name; }
            set { name = value; }
        }

        public DateTime Dob
        {
            get { return dob; }
            set { dob = value; }
        }

        public Address Address
        {
            get { return address; }
            set { address = value; }
        }

        public void ReadExternal(IPofReader reader)
        {
            id = reader.ReadInt64(0);
            name = reader.ReadString(1);
            dob = reader.ReadDate(2);
            address = (Address)reader.ReadObject(3);
        }

        public void WriteExternal(IPofWriter writer)
        {
            writer.WriteInt64(0, id);
            writer.WriteString(1, name);
            writer.WriteDate(2, dob);
            writer.WriteObject(3, address);
        }
    }
}
