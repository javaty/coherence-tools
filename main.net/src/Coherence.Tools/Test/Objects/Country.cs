using System;
using Seovic.Coherence.Core;
using Tangosol.IO.Pof;

namespace Seovic.Coherence.Test.Objects
{
    /// <summary>
    /// Helper class that is used for testing.
    /// </summary>
    public class Country : IEntity<object>, IPortableObject, IComparable
    {
        #region Data members

        private String code;
        private String name;
        private String formalName;
        private String capital;
        private String currencySymbol;
        private String currencyName;
        private String telephonePrefix;
        private String domain;

        #endregion

        #region Constructors

        public Country()
        {
        }

        public Country(String code, String name)
        {
            this.code = code;
            this.name = name;
        }

        #endregion

        #region Implementation of IEntity

        public object Id
        {
            get { return code; }
        }

        #endregion

        #region Properties

        public virtual string Code
        {
            get { return code; }
            set { code = value; }
        }

        public virtual string Name
        {
            get { return name; }
            set { name = value; }
        }

        public virtual string FormalName
        {
            get { return formalName; }
            set { formalName = value; }
        }

        public virtual string Capital
        {
            get { return capital; }
            set { capital = value; }
        }

        public virtual string CurrencySymbol
        {
            get { return currencySymbol; }
            set { currencySymbol = value; }
        }

        public virtual string CurrencyName
        {
            get { return currencyName; }
            set { currencyName = value; }
        }

        public virtual string TelephonePrefix
        {
            get { return telephonePrefix; }
            set { telephonePrefix = value; }
        }

        public virtual string Domain
        {
            get { return domain; }
            set { domain = value; }
        }

        #endregion

        #region Implementation of IPortableObject

        public void ReadExternal(IPofReader reader)
        {
            code            = reader.ReadString(0);
            name            = reader.ReadString(1);
            formalName      = reader.ReadString(2);
            capital         = reader.ReadString(3);
            currencySymbol  = reader.ReadString(4);
            currencyName    = reader.ReadString(5);
            telephonePrefix = reader.ReadString(6);
            domain          = reader.ReadString(7);
        }

        public void WriteExternal(IPofWriter writer)
        {
            writer.WriteString(0, code);
            writer.WriteString(1, name);
            writer.WriteString(2, formalName);
            writer.WriteString(3, capital);
            writer.WriteString(4, currencySymbol);
            writer.WriteString(5, currencyName);
            writer.WriteString(6, telephonePrefix);
            writer.WriteString(7, domain);
        }

        #endregion

        #region Object methods

        public bool Equals(Country obj)
        {
            if (ReferenceEquals(null, obj)) return false;
            if (ReferenceEquals(this, obj)) return true;
            return Equals(obj.code, code) 
                   && Equals(obj.name, name) 
                   && Equals(obj.formalName, formalName) 
                   && Equals(obj.capital, capital) 
                   && Equals(obj.currencySymbol, currencySymbol) 
                   && Equals(obj.currencyName, currencyName) 
                   && Equals(obj.telephonePrefix, telephonePrefix) 
                   && Equals(obj.domain, domain);
        }

        public override bool Equals(object obj)
        {
            if (ReferenceEquals(null, obj)) return false;
            if (ReferenceEquals(this, obj)) return true;
            if (obj.GetType() != typeof (Country)) return false;
            return Equals((Country) obj);
        }

        public override int GetHashCode()
        {
            unchecked
            {
                int result = (code != null ? code.GetHashCode() : 0);
                result = (result*397) ^ (name != null ? name.GetHashCode() : 0);
                result = (result*397) ^ (formalName != null ? formalName.GetHashCode() : 0);
                result = (result*397) ^ (capital != null ? capital.GetHashCode() : 0);
                result = (result*397) ^ (currencySymbol != null ? currencySymbol.GetHashCode() : 0);
                result = (result*397) ^ (currencyName != null ? currencyName.GetHashCode() : 0);
                result = (result*397) ^ (telephonePrefix != null ? telephonePrefix.GetHashCode() : 0);
                result = (result*397) ^ (domain != null ? domain.GetHashCode() : 0);
                return result;
            }
        }

        public override String ToString()
        {
            return "Country(" +
                   "Code = " + code + ", " +
                   "Name = " + name + ", " +
                   "FormalName = " + formalName + ", " +
                   "Capital = " + capital + ", " +
                   "CurrencySymbol = " + currencySymbol + ", " +
                   "CurrencyName = " + currencyName + ", " +
                   "TelephonePrefix = " + telephonePrefix + ", " +
                   "Domain = " + domain + ")";
        }

        #endregion

        #region Implementation of IComparable

        public int CompareTo(object obj)
        {
            Country other = (Country) obj;
            return name.CompareTo(other.name);
        }

        #endregion
    }
}