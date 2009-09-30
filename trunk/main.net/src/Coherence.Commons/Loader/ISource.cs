using System.Collections;
using Seovic.Coherence.Core;

namespace Seovic.Coherence.Loader
{
    public interface ISource : IEnumerable
    {
        void BeginExport();

        void EndExport();

        IExtractor GetExtractor(string propertyName);

        void SetExtractor(string propertyName, IExtractor extractor);
    }
}