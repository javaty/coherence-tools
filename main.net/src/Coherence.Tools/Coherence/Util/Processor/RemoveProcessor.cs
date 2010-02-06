using System;
using Tangosol.IO.Pof;
using Tangosol.Net.Cache;
using Tangosol.Util.Processor;

namespace Seovic.Coherence.Util.Processor
{
    public class RemoveProcessor : AbstractProcessor, IPortableObject
    {
        private bool m_fReturnOldValue;

        public RemoveProcessor()
        {
        }

        public RemoveProcessor(bool fReturnOldValue)
        {
            m_fReturnOldValue = fReturnOldValue;
        }

        #region Overrides of AbstractProcessor

        public override object Process(IInvocableCacheEntry entry)
        {
            Object oldValue = m_fReturnOldValue ? entry.Value : null;
            entry.Remove(false);
            return oldValue;
        }

        #endregion

        #region Implementation of IPortableObject

        public void ReadExternal(IPofReader reader)
        {
            m_fReturnOldValue = reader.ReadBoolean(0);
        }

        public void WriteExternal(IPofWriter writer)
        {
            writer.WriteBoolean(0, m_fReturnOldValue);
        }

        #endregion
    }
}