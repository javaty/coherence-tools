using Tangosol.Util;

namespace Seovic.Coherence.Core
{
    /// <summary>
    /// Extractor is used to extract a value from a target object.
    /// </summary>
    /// <remarks>
    /// Typically a derived value will be a single property of a target object,
    /// but it could also be a combination of multiple properties or a result
    /// of an expression or a script executed against the target object.
    /// </remarks>
    /// <author>Aleksandar Seovic  2009.06.17</author>
    public interface IExtractor : IValueExtractor
    {
    }
}
