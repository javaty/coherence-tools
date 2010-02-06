
using System.Text.RegularExpressions;

namespace Seovic.Util
{
    /// <summary>
    /// Miscellaneous string utilities.
    /// </summary>
    /// <author>Ivan Cikic  2009.17.11</author>
    public static class StringHelper
    {
        private static readonly Regex Regex = new Regex("\\b([a-z,A-Z])(\\w+)\\b");

        /// <summary>
        /// Decapitalize given string value.
        /// </summary>
        /// <param name="value">Value to decapitalize.</param>
        /// <returns>Input value with first letter converted to lower case.</returns>
        public static string Decapitalize(this string value)
        {
            return Regex.Replace(value, m => m.Groups[1].Value.ToLower() + m.Groups[2]);
        }
    }
}